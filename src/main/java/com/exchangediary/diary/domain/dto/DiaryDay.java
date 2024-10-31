package com.exchangediary.diary.domain.dto;

public record DiaryDay(
        int day,
        String profileImage,
        boolean canView
) {
}
