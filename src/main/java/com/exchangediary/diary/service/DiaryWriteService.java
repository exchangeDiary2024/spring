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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryWriteService {
    private final MemberQueryService memberQueryService;
    private final GroupQueryService groupQueryService;
    private final DiaryAuthorizationService diaryAuthorizationService;
    private final ImageService imageService;
    private final DiaryRepository diaryRepository;
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final DiaryContentRepository diaryContentRepository;

    public Long writeDiary(DiaryRequest diaryRequest, MultipartFile file, String groupId, Long memberId) {
        Member member = memberQueryService.findMember(memberId);
        Group group = groupQueryService.findGroup(groupId);

        diaryAuthorizationService.checkDiaryWritable(group, member);

        try {
            Diary diary = Diary.of(diaryRequest, member, group);
            Diary savedDiary = diaryRepository.save(diary);
            createDairyContent(diaryRequest.contents(), diary);

            imageService.saveImage(file, diary, group.getId());
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
