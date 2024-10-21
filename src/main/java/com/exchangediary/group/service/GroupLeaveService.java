package com.exchangediary.group.service;

import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.MemberRepository;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupLeaveService {
    private final MemberRepository memberRepository;
    private final MemberQueryService memberQueryService;
    private final GroupRepository groupRepository;

    public void leaveGroup(Long memberId) {
        Member member = memberQueryService.findMember(memberId);
        member.updateGroup(null);
        memberRepository.save(member);
        Group group = member.getGroup();
        group.getMembers().remove(member);
        groupRepository.save(group);
    }
}
