package com.exchangediary.group.service;

import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.group.ui.dto.response.GroupMembersResponse;
import com.exchangediary.member.domain.MemberRepository;
import com.exchangediary.member.domain.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;


import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = BEFORE_TEST_METHOD)
public class GroupQueryServiceTest {
    private static final String GROUP_NAME = "버니즈";
    @Autowired
    private GroupQueryService groupQueryService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GroupRepository groupRepository;

    @Test
    void 그룹원_정보_리스트_조회_성공() {
        Group group = createGroup();
        groupRepository.save(group);
        List<Member> members = Arrays.asList(makeMember(group, 1), makeMember(group, 2));
        memberRepository.saveAll(members);

        GroupMembersResponse response = groupQueryService.listGroupMembersInfo(group.getId());

        assertThat(response.members()).hasSize(2);
    }

    private Group createGroup() {
        return Group.builder()
                .name(GROUP_NAME)
                .currentOrder(0)
                .code("code")
                .build();
    }

    private Member makeMember(Group group, int order) {
        return Member.builder()
                .group(group)
                .kakaoId(1234L + order)
                .nickname("nickname" + order)
                .orderInGroup(order)
                .build();
    }
}
