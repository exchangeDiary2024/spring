package com.exchangediary.diary.service;

import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.diary.ui.dto.response.DiaryWritableStatusResponse;
import com.exchangediary.diary.ui.dto.response.DiaryIdResponse;
import com.exchangediary.diary.ui.dto.response.DiaryMonthlyResponse;
import com.exchangediary.diary.ui.dto.response.DiaryResponse;
import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.NotFoundException;
import com.exchangediary.group.domain.GroupRepository;
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
    private final GroupRepository groupRepository;

    public DiaryResponse viewDiary(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.DIARY_NOT_FOUND,
                        "",
                        String.valueOf(diaryId))
                );
        return DiaryResponse.of(diary);
    }

    public DiaryMonthlyResponse viewMonthlyDiary(int year, int month, Long groupId) {
        diaryValidationService.validateYearMonthFormat(year, month);
//        groupQueryService.findGroup(groupId); //Todo: 그룹 인가 구현 후 삭제 될 코드
        List<Diary> diaries = diaryRepository.findAllByGroupYearAndMonth(groupId, year, month);
        return DiaryMonthlyResponse.of(year, month, diaries);
    }

    public DiaryIdResponse findDiaryIdByDate(int year, int month, int day, Long groupId) {
        diaryValidationService.validateDateFormat(year, month, day);
        Long diaryId = diaryRepository.findIdByGroupAndDate(groupId, year, month, day)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.DIARY_NOT_FOUND,
                        "",
                        String.format("%d-%02d-%02d", year, month, day))
                );
        return DiaryIdResponse.builder()
                .diaryId(diaryId)
                .build();
    }

    public DiaryWritableStatusResponse getDiaryWritableStatus(Long groupId, Long memberId) {
        boolean writtenTodayDiary = false;
        Long diaryId = null;

        Boolean isMyOrder = groupRepository.isEqualsToGroupCurrentOrder(memberId);
        Optional<Diary> todayDiary = findTodayDiary(groupId);
        if (todayDiary.isPresent()) {
            writtenTodayDiary = true;
            diaryId = getTodayDiaryId(isMyOrder, memberId, todayDiary.get());
        }
        return DiaryWritableStatusResponse.of(isMyOrder, writtenTodayDiary, diaryId);
    }

    public Optional<Diary> findTodayDiary(Long groupId) {
        LocalDate today = LocalDate.now();
        Optional<Diary> todayDiary = diaryRepository.findByGroupAndDate(
                groupId,
                today.getYear(),
                today.getMonthValue(),
                today.getDayOfMonth()
        );
        return todayDiary;
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
