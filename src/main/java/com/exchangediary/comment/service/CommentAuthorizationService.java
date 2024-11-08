package com.exchangediary.comment.service;

import com.exchangediary.comment.domain.entity.CommentRepository;
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
                    "",
                    String.valueOf(diary.getId())
            );
        }
        if (commentRepository.existsByDiaryIdAndMemberId(member.getId(), diary.getId())) {
            throw new ForbiddenException(
                    ErrorCode.COMMENT_WRITE_FORBIDDEN,
                    "이미 댓글을 작성하였습니다.",
                    String.valueOf(diary.getId())
            );
        }
    }
}
