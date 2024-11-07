package com.exchangediary.reply.ui.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record ReplyCreateRequest(
        @NotEmpty String content
) {
}
