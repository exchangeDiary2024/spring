package com.exchangediary.group.service;

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

    public Member findMemberByNickname(Group group, String nickname) {
        return group.getMembers().stream()
                .filter(member -> member.getNickname().equals(nickname))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "",
                        nickname
                ));
    }

    public Member findCurrentOrderMember(Group group) {
        return group.getMembers().stream()
                .filter(member -> group.getCurrentOrder().equals(member.getOrderInGroup()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "",
                        String.valueOf(group.getCurrentOrder())
                ));
    }
}
