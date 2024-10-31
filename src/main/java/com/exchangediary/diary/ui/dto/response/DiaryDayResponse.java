package com.exchangediary.diary.ui.dto.response;

import com.exchangediary.diary.domain.dto.DiaryDay;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DiaryDayResponse(
        Long id,
        int day,
        String profileImage,
        boolean canView
) {
    public static DiaryDayResponse of(DiaryDay diary, LocalDate lastViewableDiaryDate) {
        return DiaryDayResponse.builder()
                .id(diary.id())
                .day(diary.createdAt().getDayOfMonth())
                .profileImage(diary.profileImage())
                .canView(!diary.createdAt().toLocalDate().isAfter(lastViewableDiaryDate))
                .build();
    }
}
