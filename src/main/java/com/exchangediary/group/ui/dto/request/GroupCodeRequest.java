package com.exchangediary.group.ui.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record GroupCodeRequest(
        @NotEmpty String code
) {
}
