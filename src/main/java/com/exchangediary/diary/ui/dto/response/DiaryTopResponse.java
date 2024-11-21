package com.exchangediary.diary.ui.dto.response;

import com.exchangediary.diary.domain.entity.Diary;
import lombok.Builder;

import java.time.format.DateTimeFormatter;

@Builder
public record DiaryTopResponse (
        String createdAt,
        String todayMood
) {
    public static DiaryTopResponse from(Diary diary) {
        return DiaryTopResponse.builder()
                .createdAt(diary.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .todayMood(diary.getTodayMood())
                .build();
    }
}
