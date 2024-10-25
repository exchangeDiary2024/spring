package com.exchangediary.diary.service;

import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.DuplicateException;
import com.exchangediary.global.exception.serviceexception.FailedImageUploadException;
import com.exchangediary.global.exception.serviceexception.InvalidDateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryValidationService {
    private static final Set<String> VALID_IMAGE_FORMAT = Set.of(
            "image/jpeg",
            "image/gif",
            "image/png",
            "image/heic",
            "image/heif"
    );
    private final DiaryRepository diaryRepository;

    public void validateYearMonthFormat(int year, int month) {
        try {
            YearMonth.of(year, month);
        } catch (DateTimeException exception) {
            throw new InvalidDateException(
                    ErrorCode.INVALID_DATE,
                    "",
                    String.format("%d-%02d", year, month)
            );
        }
    }

    public void validateDateFormat(int year, int month, int day) {
        try {
            LocalDate.of(year, month, day);
        } catch (DateTimeException exception) {
            throw new InvalidDateException(
                    ErrorCode.INVALID_DATE,
                    "",
                    String.format("%d-%02d-%02d", year, month, day)
            );
        }
    }

    public void checkTodayDiaryExistent(Long groupId) {
        Optional<Diary> todayDiary = diaryRepository.findTodayDiaryInGroup(groupId);

        if (todayDiary.isPresent()) {
            throw new DuplicateException(
                    ErrorCode.DIARY_DUPLICATED,
                    "",
                    LocalDate.now().toString()
            );
        }
    }

    public void validateImageType(MultipartFile file) {
        String contentType = file.getContentType();

        if (!VALID_IMAGE_FORMAT.contains(contentType)) {
            throw new FailedImageUploadException(
                    ErrorCode.INVALID_IMAGE_FORMAT,
                    "",
                    file.getOriginalFilename()
            );
        }
    }
}
