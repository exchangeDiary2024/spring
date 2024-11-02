package com.exchangediary.diary.ui.dto.response;

import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.entity.DiaryContent;
import lombok.Builder;

import java.util.List;

@Builder
public record DiaryResponse(
        String imageFileName,
        String nickname,
        String profileImage,
        List<DiaryContentResponse> contents
) {
    public static DiaryResponse of(Diary diary) {
        return DiaryResponse.builder()
                .imageFileName(diary.getImageFileName())
                .contents(getDiaryContent(diary.getContents()))
                .nickname(diary.getMember().getNickname())
                .profileImage(diary.getMember().getProfileImage())
                .build();
    }

    private static List<DiaryContentResponse> getDiaryContent(List<DiaryContent> diaryContents) {
        return diaryContents.stream()
                .map(diaryContent -> DiaryContentResponse.from(diaryContent.getContent()))
                .toList();
    }
}
