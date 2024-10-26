package com.exchangediary.diary.ui.dto.response;

import com.exchangediary.diary.domain.entity.Diary;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record DiaryMonthlyResponse(
        List<DiaryDayResponse> days
) {
    public static DiaryMonthlyResponse of(List<Diary> diaries, LocalDate lastViewableDiaryDate) {
        List<DiaryDayResponse> days = diaries.stream()
                .map(diary -> DiaryDayResponse.of(diary, lastViewableDiaryDate))
                .toList();
        return DiaryMonthlyResponse.builder()
                .days(days)
                .build();
    }
}
