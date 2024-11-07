package com.exchangediary.comment.ui.dto.request;

import jakarta.validation.constraints.NotNull;

public record CommentRequest(
        @NotNull Double xPosition,
        @NotNull Double yPosition,
        @NotNull String content
) {
}
