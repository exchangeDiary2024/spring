package com.exchangediary.member.ui.dto.response;

import lombok.Builder;

@Builder
public record MemberIdResponse(
        Long memberId
) {
}
