package com.exchangediary.group.ui.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record GroupJoinRequest(
        @NotEmpty String profileLocation,
        @NotEmpty String nickname
) {
}
