package com.exchangediary.diary.ui.dto.response;

import lombok.Builder;

@Builder
public record DiaryContentResponse(
        String content
) {
    public static DiaryContentResponse from(String content) {
        return DiaryContentResponse.builder()
                .content(content)
                .build();
    }
}
