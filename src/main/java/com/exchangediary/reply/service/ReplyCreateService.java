package com.exchangediary.reply.service;

import com.exchangediary.comment.domain.entity.Comment;
import com.exchangediary.comment.service.CommentQueryService;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.service.DiaryAuthorizationService;
import com.exchangediary.diary.service.DiaryQueryService;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.service.MemberQueryService;
import com.exchangediary.reply.domain.entity.Reply;
import com.exchangediary.reply.domain.entity.ReplyRepository;
import com.exchangediary.reply.ui.dto.request.ReplyCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyCreateService {
    private final MemberQueryService memberQueryService;
    private final DiaryQueryService diaryQueryService;
    private final CommentQueryService commentQueryService;
    private final DiaryAuthorizationService diaryAuthorizationService;
    private final ReplyRepository replyRepository;

    public void createReply(ReplyCreateRequest request, Long diaryId, Long commentId, Long memberId) {
        Member member = memberQueryService.findMember(memberId);
        Diary diary = diaryQueryService.findDiary(diaryId);
        Comment comment = commentQueryService.findComment(commentId);

        diaryAuthorizationService.checkDiaryViewable(member, diary);
        replyRepository.save(Reply.of(request, member, comment));
    }
}
