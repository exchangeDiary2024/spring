package com.exchangediary.diary.service;

import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.entity.UploadImage;
import com.exchangediary.diary.ui.dto.request.DiaryRequest;
import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.DuplicateException;
import com.exchangediary.global.exception.serviceexception.FailedImageUploadException;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.group.service.GroupQueryService;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryCommandService {
    private final DiaryRepository diaryRepository;
    private final MemberQueryService memberQueryService;
    private final GroupQueryService groupQueryService;


    public Long createDiary(DiaryRequest diaryRequest, MultipartFile file, Long groupId, Long memberId) {
        Member member = memberQueryService.findMember(memberId);
        Group group = groupQueryService.findGroup(groupId);
        checkTodayDiaryExistent(groupId);

        if (isEmptyFile(file)) {
            Diary diary = Diary.of(diaryRequest, null);
            Diary savedDiary = diaryRepository.save(diary);
            diary.addMemberAndGroup(member, group);
            return savedDiary.getId();
        }

        try {
            UploadImage image = UploadImage.builder()
                    .image(file.getBytes())
                    .build();
            Diary diary = Diary.of(diaryRequest, image);
            Diary savedDiary = diaryRepository.save(diary);
            diary.addMemberAndGroup(member, group);
            image.uploadToDiary(savedDiary);
            return savedDiary.getId();
        } catch (IOException e) {
            throw new FailedImageUploadException(
                    ErrorCode.FAILED_UPLOAD_IMAGE,
                    "",
                    file.getOriginalFilename()
            );
        }
    }

    private void checkTodayDiaryExistent(Long groupId) {
        LocalDate today = LocalDate.now();
        Optional<Long> todayDiary =
                diaryRepository.findIdByGroupAndDate(
                        groupId, today.getYear(), today.getMonthValue(), today.getDayOfMonth());

        if (todayDiary.isPresent()) {
            throw new DuplicateException(
                    ErrorCode.DIARY_DUPLICATED,
                    "",
                    today.toString()
            );
        }
    }

    private boolean isEmptyFile(MultipartFile file) {
        return file == null || file.isEmpty();
    }

}
