package com.exchangediary.global.config.interceptor;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.MemberRepository;
import com.exchangediary.member.domain.enums.GroupRole;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class GroupAuthorizationInterceptorTest extends ApiBaseTest {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 그룹_가입하지_않은_사용자가_그룹생성가입페이지_접근() {
        RestAssured
                .given().log().all()
                .cookie("token", token)
                .redirects().follow(false)
                .when().get("/groups")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 그룹_가입한_사용자가_그룹월별형페이지_접근() {
        Group group = createGroup();
        updateSelf(group);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .redirects().follow(false)
                .when().get("/groups/" + group.getId())
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 그룹_가입한_사용자가_그룹생성가입페이지_접근() {
        Group group = createGroup();
        updateSelf(group);

        String location = RestAssured
                .given().log().all()
                .cookie("token", token)
                .redirects().follow(false)
                .when().get("/groups")
                .then()
                .log().status()
                .log().headers()
                .statusCode(HttpStatus.FOUND.value())
                .extract()
                .header("location");

        Assertions.assertThat(location.substring(location.indexOf("/groups"))).isEqualTo("/groups/" + group.getId());
    }

    @Test
    void 그룹_가입하지_않은_사용자가_그룹월별형페이지_접근() {
        Group group = createGroup();

        String location = RestAssured
                .given().log().all()
                .cookie("token", token)
                .redirects().follow(false)
                .when().get("/groups/" + group.getId())
                .then()
                .log().status()
                .log().headers()
                .statusCode(HttpStatus.FOUND.value())
                .extract()
                .header("location");

        Assertions.assertThat(location.substring(location.indexOf("/groups"))).isEqualTo("/groups");
    }

    @Test
    void 그룹_속한_멤버_api_접근 () {
        Group group = createGroup();
        updateSelf(group);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .redirects().follow(false)
                .when().get(String.format("api/groups/%d/diaries/status", group.getId()))
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 그룹_속하지_않은_멤버_api_접근 () {
        Group group = createGroup();
        Group otherGroup = createGroup();
        updateSelf(group);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get(String.format("api/groups/%d/diaries/status", otherGroup.getId()))
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 그룹_속한_멤버_일기에_접근 () {
        Group group = createGroup();
        updateSelf(group);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .redirects().follow(false)
                .when().get(String.format("/groups/%d/diaries", group.getId()))
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 그룹_속하지_않은_멤버_일기에_접근() {
        Group group = createGroup();
        Group otherGroup = createGroup();
        updateSelf(group);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .redirects().follow(false)
                .when().get(String.format("/groups/%d/diaries", otherGroup.getId()))
                .then()
                .log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 그룹_속한_멤버_월별형_접근 () {
        Group group = createGroup();
        updateSelf(group);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .redirects().follow(false)
                .when().get("/groups/" + group.getId())
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 그룹_속하지_않은_멤버_월별형_접근() {
        Group group = createGroup();
        Group otherGroup = createGroup();
        updateSelf(group);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .redirects().follow(false)
                .when().get("/groups/" + otherGroup.getId())
                .then()
                .log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    private Group createGroup() {
        return groupRepository.save(Group.of("group-name", "code"));
    }

    private void updateSelf(Group group) {
        this.member.joinGroup(
                "me",
                "red",
                1,
                GroupRole.GROUP_MEMBER,
                group
        );
        memberRepository.save(this.member);
    }
}
