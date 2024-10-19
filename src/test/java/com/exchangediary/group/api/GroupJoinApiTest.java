package com.exchangediary.group.api;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.group.ui.dto.request.GroupJoinRequest;
import com.exchangediary.member.domain.entity.Member;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;

class GroupJoinApiTest extends ApiBaseTest {
    private static final String GROUP_NAME = "버니즈";
    private static final String API_PATH = "/api/groups/%d/join";
    @Autowired
    private GroupRepository groupRepository;

    @Test
    void 그룹_가입_성공 () {
        Group group = createGroup();
        groupRepository.save(group);
        Member groupMember = createMemberInGroup(group);
        memberRepository.save(groupMember);
        GroupJoinRequest request = new GroupJoinRequest("orange", "jisunggi");

        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .cookie("token", token)
                .when()
                .patch(String.format(API_PATH, group.getId()))
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        Member updatedMember = memberRepository.findById(member.getId()).get();
        assertThat(updatedMember.getNickname()).isEqualTo("jisunggi");
        assertThat(updatedMember.getProfileImage()).isEqualTo("orange");
        assertThat(updatedMember.getOrderInGroup()).isEqualTo(2);
        assertThat(updatedMember.getGroup().getId()).isEqualTo(group.getId());
    }

    @Test
    void 프로필_중복_그룹_가입_실패() {
        Group group = createGroup();
        groupRepository.save(group);
        Member member = createMemberInGroup(group);
        memberRepository.save(member);
        GroupJoinRequest request = new GroupJoinRequest("red", "jisunggi");

        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .cookie("token", token)
                .when()
                .patch(String.format(API_PATH, group.getId()))
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo(ErrorCode.PROFILE_DUPLICATED.getMessage()));
    }

    private Group createGroup() {
        return Group.builder()
                .name(GROUP_NAME)
                .currentOrder(0)
                .code("code")
                .build();
    }

    private Member createMemberInGroup(Group group) {
        return Member.builder()
                .kakaoId(12345L)
                .orderInGroup(1)
                .profileImage("red")
                .group(group)
                .build();
    }
}
