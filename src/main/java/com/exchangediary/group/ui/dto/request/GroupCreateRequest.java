package com.exchangediary.group.ui.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GroupCreateRequest(
        @NotBlank String groupName,
        @NotBlank String profileLocation,
        @NotBlank String nickname
) {
}
