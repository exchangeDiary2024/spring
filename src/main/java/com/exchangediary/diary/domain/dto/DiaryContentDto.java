package com.exchangediary.diary.domain.dto;

import lombok.Builder;

@Builder
public record DiaryContentDto(
        String content
) {
    public static DiaryContentDto from(String content) {
        return DiaryContentDto.builder()
                .content(content)
                .build();
    }
}
