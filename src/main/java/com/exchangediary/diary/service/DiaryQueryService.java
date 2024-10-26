package com.exchangediary.diary.service;

import com.exchangediary.diary.domain.UploadImageRepository;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.diary.domain.entity.UploadImage;
import com.exchangediary.diary.ui.dto.response.DiaryWritableStatusResponse;
import com.exchangediary.diary.ui.dto.response.DiaryIdResponse;
import com.exchangediary.diary.ui.dto.response.DiaryMonthlyResponse;
import com.exchangediary.diary.ui.dto.response.DiaryResponse;
import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.NotFoundException;
import com.exchangediary.group.service.GroupQueryService;
import com.exchangediary.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryQueryService {
    private final DiaryValidationService diaryValidationService;
    private final DiaryRepository diaryRepository;
    private final UploadImageRepository uploadImageRepository;
    private final GroupQueryService groupQueryService;
    private final MemberQueryService memberQueryService;

    public DiaryResponse viewDiary(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.DIARY_NOT_FOUND,
                        "",
                        String.valueOf(diaryId))
                );
        UploadImage uploadImage = uploadImageRepository.findByDiary(diary)
                .orElse(null);
        return DiaryResponse.of(diary, uploadImage);
    }

    public DiaryMonthlyResponse viewMonthlyDiary(int year, int month, Long groupId, Long memberId) {
        diaryValidationService.validateYearMonthFormat(year, month);
        List<Diary> diaries = diaryRepository.findAllByGroupYearAndMonth(groupId, year, month);
        LocalDate lastViewableDiaryDate = memberQueryService.getLastViewableDiaryDate(memberId);
        return DiaryMonthlyResponse.of(diaries, lastViewableDiaryDate);
    }

    public DiaryIdResponse findDiaryIdByDate(int year, int month, int day, Long groupId) {
        diaryValidationService.validateDateFormat(year, month, day);
        Long diaryId = diaryRepository.findIdByGroupAndDate(groupId, LocalDate.of(year, month, day))
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.DIARY_NOT_FOUND,
                        "",
                        String.format("%d-%02d-%02d", year, month, day))
                );
        return DiaryIdResponse.builder()
                .diaryId(diaryId)
                .build();
    }

    public DiaryWritableStatusResponse getMembersDiaryAuthorization(Long groupId, Long memberId) {
        boolean writtenTodayDiary = false;
        Long diaryId = null;

        Boolean isMyOrder = groupQueryService.isMyOrderInGroup(memberId);
        Optional<Diary> todayDiary = diaryRepository.findTodayDiaryInGroup(groupId);
        if (todayDiary.isPresent()) {
            writtenTodayDiary = true;
            diaryId = getTodayDiaryId(isMyOrder, memberId, todayDiary.get());
        }
        return DiaryWritableStatusResponse.of(isMyOrder, writtenTodayDiary, diaryId);
    }

    private Long getTodayDiaryId(Boolean isMyOrder, Long memberId, Diary todayDiary) {
        if (todayDiary.getMember().getId().equals(memberId)) {
            return todayDiary.getId();
        }
        if (isMyOrder) {
            return todayDiary.getId();
        }
        return null;
    }
}
