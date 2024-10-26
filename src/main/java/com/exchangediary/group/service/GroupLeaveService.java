package com.exchangediary.group.service;

import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.ForbiddenException;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.MemberRepository;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.domain.enums.GroupRole;
import com.exchangediary.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupLeaveService {
    private final MemberRepository memberRepository;
    private final MemberQueryService memberQueryService;
    private final GroupQueryService groupQueryService;
    private final GroupRepository groupRepository;
    private final DiaryRepository diaryRepository;

    public void leaveGroup(Long groupId, Long memberId) {
        Group group = groupQueryService.findGroup(groupId);
        Member member = memberQueryService.findMember(memberId);

        forbidGroupLeaderLeave(member, group.getMembers().size());
        int leaveMemberOrder = processMemberLeave(member);
        updateGroupAfterMemberLeave(group, leaveMemberOrder);
    }

    private void forbidGroupLeaderLeave(Member member, int numberOfGroupMember) {
        if (numberOfGroupMember > 1 && GroupRole.GROUP_LEADER.equals(member.getGroupRole())) {
            throw new ForbiddenException(ErrorCode.GROUP_LEADER_LEAVE_FORBIDDEN, "", "");
        }
    }

    private int processMemberLeave(Member member) {
        int orderInGroup = member.getOrderInGroup();
        diaryRepository.deleteByMemberId(member.getId());
        member.leaveGroup();
        memberRepository.save(member);
        return orderInGroup;
    }

    private void updateGroupAfterMemberLeave(Group group, int leaveMemberOrder) {
        if (group.getMembers().size() == 1) {
            groupRepository.delete(group);
        } else {
            updateGroupMembersOrder(group, leaveMemberOrder);
            updateGroupCurrentOrder(group, leaveMemberOrder);
        }
    }

    private void updateGroupMembersOrder(Group group, int leaveMemberOrder) {
        group.getMembers().stream()
                .filter(member -> member.getOrderInGroup() > leaveMemberOrder)
                .forEach(member -> member.updateOrderInGroup(member.getOrderInGroup() - 1));
        memberRepository.saveAll(group.getMembers());
    }

    private void updateGroupCurrentOrder(Group group, int leaveMemberOrder) {
        List<Member> members = group.getMembers();
        int currentOrder = group.getCurrentOrder();
        int numberOfGroupMember = members.size() - 1;

        if (leaveMemberOrder < currentOrder) {
            group.updateCurrentOrder(currentOrder - 1, numberOfGroupMember);
        } else {
            group.updateCurrentOrder(currentOrder, numberOfGroupMember);
        }
        groupRepository.save(group);
    }
}
