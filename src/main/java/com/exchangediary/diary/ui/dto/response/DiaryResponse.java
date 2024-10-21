package com.exchangediary.diary.ui.dto.response;

import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.entity.UploadImage;
import com.exchangediary.member.domain.entity.Member;
import lombok.Builder;

import java.time.format.DateTimeFormatter;

@Builder
public record DiaryResponse(
        Long diaryId,
        String createdAt,
        String content,
        String moodLocation,
        byte[] uploadImage,
        DiaryMemberResponse member
) {
    public static DiaryResponse from(Diary diary) {
        return DiaryResponse.builder()
                .diaryId(diary.getId())
                .createdAt(diary.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .content(diary.getContent())
                .moodLocation(diary.getMoodLocation())
                .uploadImage(getUploadImage(diary.getUploadImage()))
                .member(DiaryMemberResponse.from(diary.getMember()))
                .build();
    }

    private static byte[] getUploadImage(UploadImage uploadImage) {
        if (uploadImage == null) {
            return null;
        }
        return uploadImage.getImage();
    }

    @Builder
    private record DiaryMemberResponse(
            String nickname,
            String profileImage
    ) {
        public static DiaryMemberResponse from(Member member) {
            return DiaryMemberResponse.builder()
                    .nickname(member.getNickname())
                    .profileImage(member.getProfileImage())
                    .build();
        }
    }
}
