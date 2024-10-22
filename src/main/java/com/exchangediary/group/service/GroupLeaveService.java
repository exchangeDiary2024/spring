package com.exchangediary.group.service;

import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.MemberRepository;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        Member member = memberQueryService.findMember(memberId);
        Group group = groupQueryService.findGroup(groupId);

        updateOrderOfMembers(group, member.getOrderInGroup());
        diaryRepository.deleteByMemberId(memberId);
        member.updateMemberGroupInfo(null, null, 0, null, null);
        memberRepository.save(member);
    }

    private void updateOrderOfMembers(Group group, Integer orderInGroup) {
        List<Member> members = group.getMembers();

        List<Member> largerOrderMembers = members.stream()
                .filter(m -> m.getOrderInGroup() > orderInGroup)
                .toList();
        if (largerOrderMembers.isEmpty()) {
            group.updateCurrentOrder(1);
        }
        else {
            largerOrderMembers.forEach(m -> m.updateOrderInGroup(m.getOrderInGroup() - 1));
        }
        memberRepository.saveAll(members);
        groupRepository.save(group);
    }
}
