package com.exchangediary.group.service;

import com.exchangediary.diary.service.DiaryQueryService;
import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.NotFoundException;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.domain.enums.GroupRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupMemberService {
    private final DiaryQueryService diaryQueryService;

    public Member findSelfInGroup(Group group, Long memberId) {
        return group.getMembers().stream()
                .filter(member -> memberId.equals(member.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "",
                        String.valueOf(memberId)
                ));
    }

    public Member findGroupLeader(Group group) {
        return group.getMembers().stream()
                .filter(member -> GroupRole.GROUP_LEADER.equals(member.getGroupRole()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.GROUP_LEADER_NOT_FOUND,
                        "",
                        String.valueOf(group.getId())
                ));
    }

    public Member findMemberHasWriteAuthority(Group group) {
        if (diaryQueryService.findTodayDiary(group.getId()).isPresent()) {
            return group.getMembers().get(group.getCurrentOrder() - 2);
        }
        return group.getMembers().get(group.getCurrentOrder() - 1);
    }
}
