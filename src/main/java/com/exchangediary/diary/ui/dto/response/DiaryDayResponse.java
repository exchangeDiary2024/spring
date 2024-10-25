package com.exchangediary.diary.ui.dto.response;

import com.exchangediary.diary.domain.entity.Diary;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DiaryDayResponse(
        int day,
        String profileImage,
        boolean canView
) {
    public static DiaryDayResponse of(Diary diary, LocalDate lastViewableDiaryDate) {
        return DiaryDayResponse.builder()
                .day(diary.getCreatedAt().getDayOfMonth())
                .profileImage(diary.getMember().getProfileImage())
                .canView(!diary.getCreatedAt().toLocalDate().isAfter(lastViewableDiaryDate))
                .build();

    }
}
