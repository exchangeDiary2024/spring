package com.exchangediary.diary.service;

import com.exchangediary.diary.domain.DiaryContentRepository;
import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.diary.ui.dto.request.DiaryContentRequest;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.entity.DiaryContent;
import com.exchangediary.diary.ui.dto.request.DiaryRequest;
import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.FailedImageUploadException;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.group.service.GroupQueryService;
import com.exchangediary.member.domain.MemberRepository;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryWriteService {
    private final MemberQueryService memberQueryService;
    private final GroupQueryService groupQueryService;
    private final DiaryValidationService diaryValidationService;
    private final DiaryAuthorizationService diaryAuthorizationService;
    private final DiaryRepository diaryRepository;
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final DiaryContentRepository diaryContentRepository;

    public Long writeDiary(DiaryRequest diaryRequest, MultipartFile file, Long groupId, Long memberId) {
        Member member = memberQueryService.findMember(memberId);
        Group group = groupQueryService.findGroup(groupId);

        diaryAuthorizationService.checkDiaryWritable(group, member);

        try {
            Diary diary = Diary.of(diaryRequest, member, group);
            Diary savedDiary = diaryRepository.save(diary);
            createDairyContent(diaryRequest.contents(), diary);

            saveImage(file, diary, group.getId());
            updateGroupCurrentOrder(group);
            updateViewableDiaryDate(member, group);
            member.updateLastViewableDiaryDate();

            return savedDiary.getId();
        } catch (IOException e) {
            throw new FailedImageUploadException(
                    ErrorCode.FAILED_UPLOAD_IMAGE,
                    "네트워크 오류로 인해 \n일기 업로드에 실패했습니다.\n다시 시도해주세요.",
                    file.getOriginalFilename()
            );
        }
    }

    private void createDairyContent(List<DiaryContentRequest> contents, Diary diary) {
        List<DiaryContent> diaryContents = new ArrayList<>();
        int index = 0;

        while (index < contents.size()) {
            diaryContents.add(DiaryContent.of(index + 1, contents.get(index).content(), diary));
            index++;
        }
        diaryContentRepository.saveAll(diaryContents);
    }

    private void saveImage(MultipartFile file, Diary diary, Long groupId) throws IOException{
        if (!isEmptyFile(file)) {
            diaryValidationService.validateImageType(file);
            String imagePath = System.getProperty("user.dir") + "/src/main/resources/static/images/upload/groups/" + groupId;
            File directory = new File(imagePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String fileExtension = getFileExtension(file.getOriginalFilename());
            file.transferTo(new File(imagePath + "/" + date + fileExtension));
            diary.uploadImageFileName(date + fileExtension);
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return fileName.substring(dotIndex);
    }

    private boolean isEmptyFile(MultipartFile file) {
        return file == null || file.isEmpty();
    }

    private void updateGroupCurrentOrder(Group group) {
        int currentOrder = group.getCurrentOrder() + 1;
        group.updateCurrentOrder(currentOrder, group.getMembers().size());
        groupRepository.save(group);
    }

    private void updateViewableDiaryDate(Member currentWriter, Group group) {
        Member nextWriter = group.getMembers().stream()
                        .filter(member -> group.getCurrentOrder().equals(member.getOrderInGroup()))
                        .findFirst()
                        .get();

        nextWriter.updateLastViewableDiaryDate();
        currentWriter.updateLastViewableDiaryDate();
        memberRepository.saveAll(Arrays.asList(currentWriter, nextWriter));
    }
}
