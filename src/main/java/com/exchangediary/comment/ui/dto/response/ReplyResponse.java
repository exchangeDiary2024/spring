package com.exchangediary.comment.ui.dto.response;

import com.exchangediary.comment.domain.entity.Reply;
import lombok.Builder;

@Builder
public record ReplyResponse(
        String profileImage,
        String content
) {
    public static ReplyResponse from(Reply reply) {
        return ReplyResponse.builder()
                .profileImage(reply.getMember().getProfileImage())
                .content(reply.getContent())
                .build();
    }
}
