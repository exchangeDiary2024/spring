package com.exchangediary.diary.ui.dto.response;

import com.exchangediary.diary.domain.dto.DiaryContentDto;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.entity.DiaryContent;
import lombok.Builder;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder
public record DiaryResponse(
        String createdAt,
        String moodLocation,
        String imageFileName,
        String nickname,
        String profileImage,
        List<DiaryContentDto> contents
) {
    public static DiaryResponse of(Diary diary) {
        return DiaryResponse.builder()
                .createdAt(diary.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .moodLocation(diary.getMoodLocation())
                .imageFileName(diary.getImageFileName())
                .contents(getDiaryContent(diary.getContents()))
                .nickname(diary.getMember().getNickname())
                .profileImage(diary.getMember().getProfileImage())
                .build();
    }

    private static List<DiaryContentDto> getDiaryContent(List<DiaryContent> diaryContents) {
        List<DiaryContentDto> diaryContentsList = diaryContents.stream()
                .map(diaryContent -> DiaryContentDto.from(diaryContent.getContent()))
                .toList();

        return diaryContentsList;
    }
}
