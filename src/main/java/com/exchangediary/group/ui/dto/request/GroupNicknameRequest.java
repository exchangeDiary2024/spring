package com.exchangediary.group.ui.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GroupNicknameRequest(
        @NotBlank(message = "닉네임에 공백이 포함 될 수 없습니다.") String nickname
) {
}
