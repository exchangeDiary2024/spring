package com.exchangediary.diary.ui.dto.response;

import com.exchangediary.diary.domain.entity.Diary;
import lombok.Builder;

import java.time.format.DateTimeFormatter;

@Builder
public record DiaryTopResponse (
        String createdAt,
        String moodLocation
) {
    public static DiaryTopResponse of(Diary diary) {
        return DiaryTopResponse.builder()
                .createdAt(diary.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .moodLocation(diary.getMoodLocation())
                .build();
    }
}
