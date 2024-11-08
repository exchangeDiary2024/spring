package com.exchangediary.comment.ui.dto.response;

import com.exchangediary.comment.domain.entity.Comment;
import lombok.Builder;

@Builder
public record CommentCreateResponse(
        Long commentId
) {
    public static CommentCreateResponse from(Comment comment) {
        return CommentCreateResponse.builder()
                .commentId(comment.getId())
                .build();
    }
}
