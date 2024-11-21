package com.exchangediary.comment.ui.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CommentCreateRequest(
        @NotNull Double xCoordinate,
        @NotNull Double yCoordinate,
        @NotNull Integer page,
        @NotEmpty String content
) {
}
