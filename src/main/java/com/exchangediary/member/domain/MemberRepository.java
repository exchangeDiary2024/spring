package com.exchangediary.member.domain;

import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.dto.GroupId;
import com.exchangediary.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBykakaoId(Long kakaoId);
    List<Member> findAllByGroupOrderByOrderInGroup(Group group);
    Optional<GroupId> findGroupIdById(Long memberId);
    @Query("SELECT COUNT(g.id) > 0 FROM Group g JOIN g.members m WHERE m.id = :memberId AND m.groupRole = 'GROUP_LEADER'")
    boolean isGroupLeader(Long memberId);
}
