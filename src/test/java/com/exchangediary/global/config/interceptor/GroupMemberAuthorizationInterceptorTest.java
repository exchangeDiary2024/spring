package com.exchangediary.global.config.interceptor;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.MemberRepository;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.domain.enums.GroupRole;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class GroupMemberAuthorizationInterceptorTest extends ApiBaseTest {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 그룹_속한_멤버_api_접근 () {
        Group group = createGroup();
        updateSelfInfo(member, group);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get(String.format("api/groups/%d/diaries/status", group.getId()))
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 그룹_속하지_않은_멤버_api_접근 () {
        Group group = createGroup();
        Group otherGroup = createGroup();
        updateSelfInfo(member, group);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get(String.format("api/groups/%d/diaries/status", otherGroup.getId()))
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void 그룹_속한_멤버_일기에_접근 () {
        Group group = createGroup();
        updateSelfInfo(member, group);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get(String.format("/group/%d/diary", group.getId()))
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 그룹_속하지_않은_멤버_일기에_접근() {
        Group group = createGroup();
        Group otherGroup = createGroup();
        updateSelfInfo(member, group);

        String location = RestAssured
                .given().log().all()
                .cookie("token", token)
                .redirects().follow(false)
                .when().get(String.format("/group/%d/diary", otherGroup.getId()))
                .then()
                .log().status()
                .log().headers()
                .statusCode(HttpStatus.FOUND.value())
                .extract()
                .header("Location");

        assertThat(location.substring(location.indexOf("/group"))).isEqualTo("/group/" + group.getId());
    }

    @Test
    void 그룹_속한_멤버_월별형_접근 () {
        Group group = createGroup();
        updateSelfInfo(member, group);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get("/group/" + group.getId())
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 그룹_속하지_않은_멤버_월별형_접근() {
        Group group = createGroup();
        Group otherGroup = createGroup();
        updateSelfInfo(member, group);

        String location = RestAssured
                .given().log().all()
                .cookie("token", token)
                .redirects().follow(false)
                .when().get("/group/" + otherGroup.getId())
                .then()
                .log().status()
                .log().headers()
                .statusCode(HttpStatus.FOUND.value())
                .extract()
                .header("Location");

        assertThat(location.substring(location.indexOf("/group"))).isEqualTo("/group/" + group.getId());
    }

    private Group createGroup() {
        return groupRepository.save(Group.of("group-name", "code"));
    }

    private Member updateSelfInfo(Member member, Group group) {
        member.joinGroup(
                "api요청멤버",
                "orange",
                1,
                GroupRole.GROUP_MEMBER,
                group);
        memberRepository.save(member);
        return member;
    }
}
