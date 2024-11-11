package com.exchangediary.group.service;

import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.ForbiddenException;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.group.ui.dto.request.GroupKickOutRequest;
import com.exchangediary.group.ui.dto.request.GroupLeaderHandOverRequest;
import com.exchangediary.member.domain.MemberRepository;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.domain.enums.GroupRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupLeaderService {
    private final GroupQueryService groupQueryService;
    private final GroupLeaveService groupLeaveService;
    private final GroupMemberService groupMemberService;
    private final GroupValidationService groupValidationService;
    private final MemberRepository memberRepository;

    public void handOverGroupLeader(String groupId, Long memberId, GroupLeaderHandOverRequest request) {
        Group group = groupQueryService.findGroup(groupId);
        Member currentLeader = groupMemberService.findSelfInGroup(group, memberId);
        Member newLeader = groupMemberService.findMemberByNickname(group, request.nickname());

        currentLeader.changeGroupRole(GroupRole.GROUP_MEMBER);
        newLeader.changeGroupRole(GroupRole.GROUP_LEADER);
    }

    public void skipDiaryOrder(String groupId) {
        Group group = groupQueryService.findGroup(groupId);
        groupValidationService.checkSkipOrderAuthority(group);
        group.updateCurrentOrder(group.getCurrentOrder() + 1, group.getMembers().size());
        group.updateLastSkipOrderDate();
        Member currentWriter = groupMemberService.findCurrentOrderMember(group);
        currentWriter.updateLastViewableDiaryDate();
    }

    public void kickOutMember(String groupId, GroupKickOutRequest request) {
        Group group = groupQueryService.findGroup(groupId);
        Member kickMember = groupMemberService.findMemberByNickname(group, request.nickname());
        groupLeaveService.leaveGroup(groupId, kickMember.getId());
    }

    public boolean isGroupLeader(Long memberId) {
        if (!memberRepository.isGroupLeader(memberId)) {
            throw new ForbiddenException(ErrorCode.GROUP_LEADER_FORBIDDEN, "", "");
        }
        return true;
    }
}
