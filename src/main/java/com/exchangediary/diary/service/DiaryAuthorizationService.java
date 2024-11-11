package com.exchangediary.diary.service;

import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.ForbiddenException;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.group.service.GroupQueryService;
import com.exchangediary.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryAuthorizationService {
    private final GroupQueryService groupQueryService;
    private final DiaryRepository diaryRepository;

    public void checkDiaryWritable(Group group, Member member) {
        if (!group.getCurrentOrder().equals(member.getOrderInGroup())) {
            throw new ForbiddenException(ErrorCode.DIARY_WRITE_FORBIDDEN, "", String.valueOf(member.getId()));
        }
        if (diaryRepository.existsTodayDiaryInGroup(group.getId())) {
            throw new ForbiddenException(ErrorCode.DIARY_WRITE_FORBIDDEN, "", LocalDate.now().toString());
        }
    }

    public void checkDiaryWritable(String groupId, Long memberId) {
        if (!groupQueryService.isMyOrderInGroup(memberId)) {
            throw new ForbiddenException(ErrorCode.DIARY_WRITE_FORBIDDEN, "", String.valueOf(memberId));
        }
        if (diaryRepository.existsTodayDiaryInGroup(groupId)) {
            throw new ForbiddenException(ErrorCode.DIARY_WRITE_FORBIDDEN, "", LocalDate.now().toString());
        }
    }

    public void checkDiaryViewable(Member member, Diary diary) {
        if (member.getLastViewableDiaryDate().isBefore(diary.getCreatedAt().toLocalDate())) {
            throw new ForbiddenException(ErrorCode.DIARY_VIEW_FORBIDDEN, "", member.getLastViewableDiaryDate().toString());
        }
    }
}
