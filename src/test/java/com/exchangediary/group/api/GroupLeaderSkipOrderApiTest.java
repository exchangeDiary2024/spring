package com.exchangediary.group.api;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.domain.enums.GroupRole;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class GroupLeaderSkipOrderApiTest extends ApiBaseTest {
    @Autowired
    private GroupRepository groupRepository;

    @Test
    void 일기_건너뛰기_성공() {
        Group group = createGroup(2, LocalDate.now().minusDays(1));
        updateSelf(group, 1, GroupRole.GROUP_LEADER);
        createMember(group, 2, GroupRole.GROUP_MEMBER);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().patch("/api/group/" + group.getId() + "/leader/skip-order")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        int result = groupRepository.findById(group.getId()).get().getCurrentOrder();
        assertThat(result).isEqualTo(1);
    }

    @Test
    void 일기_건너뛰기_실패_오늘_이미_실행() {
        Group group = createGroup(2, LocalDate.now());
        updateSelf(group, 1, GroupRole.GROUP_LEADER);
        createMember(group, 2, GroupRole.GROUP_MEMBER);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().patch("/api/group/" + group.getId() + "/leader/skip-order")
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value());

        int result = groupRepository.findById(group.getId()).get().getCurrentOrder();
        assertThat(result).isEqualTo(2);
    }

    private Group createGroup(int order, LocalDate lastDate) {
        Group group = Group.of("group-name", "code", lastDate);
        group.updateCurrentOrder(order, 2);
        return groupRepository.save(group);
    }

    private void updateSelf(Group group, int order, GroupRole role) {
        this.member.updateMemberGroupInfo(
                "me",
                "red",
                order,
                role,
                group
        );
        memberRepository.save(this.member);
    }

    private Member createMember(Group group, int order, GroupRole role) {
        return memberRepository.save(Member.builder()
                .kakaoId(1L)
                .nickname("group-member")
                .profileImage("orange")
                .orderInGroup(order)
                .group(group)
                .groupRole(role)
                .build()
        );
    }
}
