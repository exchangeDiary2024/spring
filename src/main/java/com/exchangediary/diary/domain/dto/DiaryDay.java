package com.exchangediary.diary.domain.dto;

import java.time.LocalDateTime;

public record DiaryDay(
        Long id,
        LocalDateTime createdAt,
        String profileImage
) {
}
