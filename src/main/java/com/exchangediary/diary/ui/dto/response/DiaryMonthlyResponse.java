package com.exchangediary.diary.ui.dto.response;

import com.exchangediary.diary.domain.entity.Diary;
import lombok.Builder;

import java.util.List;

@Builder
public record DiaryMonthlyResponse(
        int year,
        int month,
        List<DiaryInfo> days
) {
    public static DiaryMonthlyResponse of(int year, int month, List<Diary> diaries) {
        List<DiaryInfo> diaryInfos = diaries.stream()
                .map(DiaryInfo::of)
                .toList();
        return DiaryMonthlyResponse.builder()
                .year(year)
                .month(month)
                .days(diaryInfos)
                .build();
    }

    @Builder
    private record DiaryInfo(
            int date,
            String profileImage
    ) {
        public static DiaryInfo of(Diary diary) {
            return DiaryInfo.builder()
                    .date(diary.getCreatedAt().getDayOfMonth())
                    .profileImage(diary.getMember().getProfileImage())
                    .build();
        }
    }
}
