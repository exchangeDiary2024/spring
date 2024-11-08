package com.exchangediary.comment.service;

import com.exchangediary.comment.domain.entity.Comment;
import com.exchangediary.comment.domain.entity.CommentRepository;
import com.exchangediary.comment.ui.dto.request.CommentCreateRequest;
import com.exchangediary.comment.ui.dto.response.CommentCreateResponse;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.service.DiaryAuthorizationService;
import com.exchangediary.diary.service.DiaryQueryService;
import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.ForbiddenException;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final MemberQueryService memberQueryService;
    private final DiaryQueryService diaryQueryService;
    private final DiaryAuthorizationService diaryAuthorizationService;
    private final CommentRepository commentRepository;

    public CommentCreateResponse createComment(CommentCreateRequest request, Long diaryId, Long memberId) {
        Member member = memberQueryService.findMember(memberId);
        Diary diary = diaryQueryService.findDiary(diaryId);

        diaryAuthorizationService.checkDiaryViewable(member, diary);
        checkCommentWritable(member, diary);
        Comment comment = Comment.of(request, member, diary);
        commentRepository.save(comment);
        return CommentCreateResponse.from(comment);
    }

    public void checkCommentWritable(Member member, Diary diary) {
        if (member == diary.getMember()) {
            throw new ForbiddenException(ErrorCode.COMMENT_WRITE_FORBIDDEN, "", "");
        }
        if (commentRepository.existsByDiaryIdAndMemberId(member.getId(), diary.getId())) {
            throw new ForbiddenException(ErrorCode.COMMENT_WRITE_FORBIDDEN, "이미 댓글을 작성하였습니다.", "");
        }
    }
}
