package com.exchangediary.diary.service;

import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.entity.UploadImage;
import com.exchangediary.diary.ui.dto.request.DiaryRequest;
import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.FailedImageUploadException;
import com.exchangediary.global.exception.serviceexception.ForbiddenException;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.group.service.GroupQueryService;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryCommandService {
    private final MemberQueryService memberQueryService;
    private final GroupQueryService groupQueryService;
    private final DiaryValidationService diaryValidationService;
    private final DiaryRepository diaryRepository;
    private final GroupRepository groupRepository;

    public Long createDiary(DiaryRequest diaryRequest, MultipartFile file, Long groupId, Long memberId) {
        Member member = memberQueryService.findMember(memberId);
        Group group = groupQueryService.findGroup(groupId);
        diaryValidationService.checkTodayDiaryExistent(groupId);
        //Todo: 일기 작성 인가 후 삭제
        if (!member.getOrderInGroup().equals(group.getCurrentOrder())) {
            throw new ForbiddenException(
                    ErrorCode.DIARY_WRITE_FORBIDDEN,
                    "",
                    String.valueOf(group.getCurrentOrder())
            );
        }

        if (isEmptyFile(file)) {
            Diary diary = Diary.of(diaryRequest, null);
            changeCurrentOrderOfGroup(group);
            diary.addMemberAndGroup(member, group);
            Diary savedDiary = diaryRepository.save(diary);
            return savedDiary.getId();
        }

        diaryValidationService.validateImageType(file);
        try {
            UploadImage image = UploadImage.builder()
                    .image(file.getBytes())
                    .build();
            Diary diary = Diary.of(diaryRequest, image);
            changeCurrentOrderOfGroup(group);
            diary.addMemberAndGroup(member, group);
            Diary savedDiary = diaryRepository.save(diary);
            image.uploadToDiary(savedDiary);
            return savedDiary.getId();
        } catch (IOException e) {
            throw new FailedImageUploadException(
                    ErrorCode.FAILED_UPLOAD_IMAGE,
                    "네트워크 오류로 인해 \n일기 업로드에 실패했습니다.\n다시 시도해주세요.",
                    file.getOriginalFilename()
            );
        }
    }

    private boolean isEmptyFile(MultipartFile file) {
        return file == null || file.isEmpty();
    }

    private void changeCurrentOrderOfGroup(Group group) {
        int currentOrder = group.getCurrentOrder() + 1;
        if (group.getMembers().size() < currentOrder)
            currentOrder = 1;
        group.updateCurrentOrder(currentOrder);
        groupRepository.save(group);
    }
}
