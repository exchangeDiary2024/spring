package com.exchangediary.group.api;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.entity.Member;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.hamcrest.core.IsEqual.equalTo;

class GroupNicknameApiTest extends ApiBaseTest {
    private static final String GROUP_NAME = "버니즈";
    private static final String API_PATH = "/api/groups/%d/nickname/verify";
    @Autowired
    private GroupRepository groupRepository;

    @Test
    void 닉네임_유효성_검사_성공() {
        Group group = createGroup();
        groupRepository.save(group);

        RestAssured
                .given().log().all()
                .queryParam("nickname", "jisunggi")
                .cookie("token", token)
                .when().get(String.format(API_PATH, group.getId()))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("verification", equalTo(true));
    }

    @Test
    void 닉네임_유효성_검사_중복() {
        Group group = createGroup();
        groupRepository.save(group);
        Member member = createMember(group);
        memberRepository.save(member);

        RestAssured
                .given().log().all()
                .queryParam("nickname", "jisunggi")
                .cookie("token", token)
                .when().get(String.format(API_PATH, group.getId()))
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private Group createGroup() {
        return Group.builder()
                .name(GROUP_NAME)
                .currentOrder(0)
                .code("code")
                .build();
    }

    private Member createMember(Group group) {
        return Member.builder()
                .nickname("jisunggi")
                .kakaoId(12345L)
                .group(group)
                .build();
    }
}
