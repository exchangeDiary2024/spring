package com.exchangediary.diary.ui.dto.response;

import com.exchangediary.diary.domain.entity.Diary;
import lombok.Builder;

import java.util.List;

@Builder
public record DiaryResponse(
        String imageFileName,
        String nickname,
        String profileImage,
        List<DiaryContentResponse> contents,
        List<DiaryCommentResponse> comments
) {
    public static DiaryResponse of(Diary diary) {
        return DiaryResponse.builder()
                .imageFileName(diary.getImageFileName())
                .nickname(diary.getMember().getNickname())
                .profileImage(diary.getMember().getProfileImage())
                .contents(DiaryContentResponse.fromContents(diary.getContents()))
                .comments(DiaryCommentResponse.fromComments(diary.getComments()))
                .build();
    }
}
