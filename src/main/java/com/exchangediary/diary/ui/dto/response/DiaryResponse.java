package com.exchangediary.diary.ui.dto.response;

import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.entity.UploadImage;
import lombok.Builder;

import java.time.format.DateTimeFormatter;

@Builder
public record DiaryResponse(
        Long diaryId,
        String createdAt,
        String content,
        String moodLocation,
        byte[] uploadImage
) {
    public static DiaryResponse of(Diary diary) {
        return DiaryResponse.builder()
                .diaryId(diary.getId())
                .createdAt(diary.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .content(diary.getContent())
                .moodLocation(diary.getMoodLocation())
                .uploadImage(getUploadImage(diary.getUploadImage()))
                .build();
    }

    private static byte[] getUploadImage(UploadImage uploadImage) {
        if (uploadImage == null) {
            return null;
        }
        return uploadImage.getImage();
    }
}
