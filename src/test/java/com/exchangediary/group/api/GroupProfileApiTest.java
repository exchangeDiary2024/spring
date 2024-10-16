package com.exchangediary.group.api;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.group.ui.dto.response.GroupProfileResponse;
import com.exchangediary.member.domain.entity.Member;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GroupProfileApiTest extends ApiBaseTest {
    private static final String GROUP_NAME = "버니즈";
    private static final String API_PATH = "/api/groups/%d/profile-image";
    @Autowired
    private GroupRepository groupRepository;

    @Test
    void 프로필_이미지_선택_목록_조회_성공() {
        Group group = createGroup();
        groupRepository.save(group);
        Member member1 = createMember(group, 1);
        Member member2 = createMember(group, 2);
        memberRepository.saveAll(List.of(member1, member2));

        GroupProfileResponse response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get(String.format(API_PATH, group.getId()))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(GroupProfileResponse.class);

        assertThat(response.selectedImages()).hasSize(2);
    }

    @Test
    void 프로필_이미지_선택_목록_조회_그룹원_없을때() {
        Group group = createGroup();
        groupRepository.save(group);

        GroupProfileResponse response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get(String.format(API_PATH, group.getId()))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(GroupProfileResponse.class);

        assertThat(response.selectedImages()).hasSize(0);
    }

    @Test
    void 프로필_이미지_선택_목록_조회_그룹원_다참() {
        Group group = createGroup();
        groupRepository.save(group);
        List<Member> members = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            members.add(createMember(group, i));
        }
        memberRepository.saveAll(members);

        GroupProfileResponse response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get(String.format(API_PATH, group.getId()))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(GroupProfileResponse.class);


        assertThat(response.selectedImages()).hasSize(7);
    }

    @Test
    void 프로필_이미지_선택_목록_조회_그룹없음() {
        Long groupId = 100L;

        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get(String.format(API_PATH, groupId))
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private Member createMember(Group group, int index) {
        return Member.builder()
                .profileLocation("resource/profile-Image" + index)
                .kakaoId(1234L + index)
                .group(group)
                .build();
    }

    private Group createGroup() {
        return Group.builder()
                .name(GROUP_NAME)
                .currentOrder(0)
                .code("code")
                .build();
    }
}
