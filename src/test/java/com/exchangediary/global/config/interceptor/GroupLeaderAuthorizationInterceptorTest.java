package com.exchangediary.global.config.interceptor;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.group.ui.dto.request.GroupLeaderHandOverRequest;
import com.exchangediary.member.domain.MemberRepository;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.domain.enums.GroupRole;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class GroupLeaderAuthorizationInterceptorTest extends ApiBaseTest {
    private static final String API_PATH = "/api/groups/%s/leader";
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 방장_권한_성공_방장넘기기 () {
        Group group = createGroup();
        updateSelfInfo(member, GroupRole.GROUP_LEADER, group);
        Member groupMember = createMember(group, 2, GroupRole.GROUP_MEMBER);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(new GroupLeaderHandOverRequest("api요청멤버"))
                .when().patch(String.format(API_PATH + "/hand-over", group.getId()))
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 방장_권한_성공_순서넘기기 () {
        Group group = createGroup();
        updateSelfInfo(member, GroupRole.GROUP_LEADER, group);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().patch(String.format(API_PATH + "/skip-order", group.getId()))
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 방장_권한_실패_순서넘기기 () {
        Group group = createGroup();
        updateSelfInfo(member, GroupRole.GROUP_MEMBER, group);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().patch(String.format(API_PATH + "/skip-order", group.getId()))
                .then()
                .log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 방장_권한_실패_방장넘기기 () {
        Group group = createGroup();
        updateSelfInfo(member, GroupRole.GROUP_MEMBER, group);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(new GroupLeaderHandOverRequest("api요청멤버"))
                .when().patch(String.format(API_PATH + "/hand-over", group.getId()))
                .then()
                .log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    private Group createGroup() {
        return groupRepository.save(Group.of("group-name"));
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

    private Member updateSelfInfo(Member member, GroupRole role, Group group) {
        member.joinGroup(
                "api요청멤버",
                "orange",
                1,
                role,
                group);
        memberRepository.save(member);
        return member;
    }
}
