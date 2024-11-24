package com.exchangediary.comment.service;

import com.exchangediary.comment.domain.CommentRepository;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.ForbiddenException;
import com.exchangediary.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentAuthorizationService {
    private final CommentRepository commentRepository;

    public void checkCommentWritable(Member member, Diary diary) {
        if (member == diary.getMember()) {
            throw new ForbiddenException(
                    ErrorCode.COMMENT_WRITE_FORBIDDEN,
                    "내 일기에는 댓글을 남길 수 없어요!",
                    String.valueOf(diary.getId())
            );
        }
        if (commentRepository.existsByMemberIdAndDiaryId(member.getId(), diary.getId())) {
            throw new ForbiddenException(
                    ErrorCode.COMMENT_WRITE_FORBIDDEN,
                    "댓글은 한 번 남길 수 있어요!",
                    String.valueOf(diary.getId())
            );
        }
    }
}
