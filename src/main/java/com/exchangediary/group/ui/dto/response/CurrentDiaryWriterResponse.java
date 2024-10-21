package com.exchangediary.group.ui.dto.response;

import lombok.Builder;

@Builder
public record CurrentDiaryWriterResponse(
        int index,
        boolean isOverdue
) {
    public static CurrentDiaryWriterResponse of(int index, boolean isOverdue) {
        return CurrentDiaryWriterResponse.builder()
                .index(index)
                .isOverdue(isOverdue)
                .build();
    }
}