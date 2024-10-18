package com.exchangediary.group.ui.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record GroupCodeRequest(
        @NotEmpty(message = "그룹코드를 입력해주세요.") String code
) {
}
