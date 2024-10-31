package com.exchangediary.diary.ui.dto.response;

import com.exchangediary.diary.domain.dto.DiaryDay;
import lombok.Builder;

import java.util.List;

@Builder
public record DiaryMonthlyResponse(
        List<DiaryDay> days
) {
    public static DiaryMonthlyResponse from(List<DiaryDay> diaries) {
        return DiaryMonthlyResponse.builder()
                .days(diaries)
                .build();
    }
}
