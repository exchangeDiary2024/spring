package com.exchangediary.diary.ui.dto.response;

import com.exchangediary.diary.domain.dto.DiaryContentDto;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.entity.DiaryContent;
import lombok.Builder;

import java.util.List;

@Builder
public record DiaryResponse(
        String imageFileName,
        String nickname,
        String profileImage,
        List<DiaryContentDto> contents
) {
    public static DiaryResponse of(Diary diary) {
        return DiaryResponse.builder()
                .imageFileName(diary.getImageFileName())
                .contents(getDiaryContent(diary.getContents()))
                .nickname(diary.getMember().getNickname())
                .profileImage(diary.getMember().getProfileImage())
                .build();
    }

    private static List<DiaryContentDto> getDiaryContent(List<DiaryContent> diaryContents) {
        return diaryContents.stream()
                .map(diaryContent -> DiaryContentDto.from(diaryContent.getContent()))
                .toList();
    }
}
