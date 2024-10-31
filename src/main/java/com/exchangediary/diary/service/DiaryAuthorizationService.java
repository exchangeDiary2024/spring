package com.exchangediary.diary.service;

import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.ForbiddenException;
import com.exchangediary.group.service.GroupQueryService;
import com.exchangediary.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryAuthorizationService {
    private final GroupQueryService groupQueryService;
    private final DiaryRepository diaryRepository;

    public boolean canWriteDiary(Long memberId, Long groupId) {
        if (!groupQueryService.isMyOrderInGroup(memberId)) {
            throw new ForbiddenException(ErrorCode.DIARY_WRITE_FORBIDDEN, "", "");
        }
        if (diaryRepository.existsTodayDiaryInGroup(groupId)) {
            throw new ForbiddenException(ErrorCode.DIARY_WRITE_FORBIDDEN, "", "");
        }
        return true;
    }

    public void checkViewableDiary(Member member, Diary diary) {
        if (member.getLastViewableDiaryDate().isBefore(diary.getCreatedAt().toLocalDate())) {
            throw new ForbiddenException(ErrorCode.DIARY_VIEW_FORBIDDEN, "", "");
        }
    }
}
