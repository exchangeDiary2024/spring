package com.exchangediary.diary.ui.dto.response;

import com.exchangediary.diary.domain.dto.DiaryContentDto;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.entity.DiaryContent;
import com.exchangediary.member.domain.entity.Member;
import lombok.Builder;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder
public record DiaryResponse(
        String createdAt,
        String moodLocation,
        String imageFileName,
        DiaryMemberResponse member,
        List<DiaryContentDto> contents
) {
    public static DiaryResponse of(Diary diary) {
        return DiaryResponse.builder()
                .createdAt(diary.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .moodLocation(diary.getMoodLocation())
                .imageFileName(diary.getImageFileName())
                .contents(getDiaryContent(diary.getContents()))
                .member(DiaryMemberResponse.from(diary.getMember()))
                .build();
    }

    private static List<DiaryContentDto> getDiaryContent(List<DiaryContent> diaryContents) {
        List<DiaryContentDto> diaryContentsList = diaryContents.stream()
                .map(diaryContent -> DiaryContentDto.from(diaryContent.getContent()))
                .toList();

        return diaryContentsList;
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
