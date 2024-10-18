package com.exchangediary.group.api;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.group.ui.dto.request.GroupCreateRequest;
import com.exchangediary.group.ui.dto.response.GroupCreateResponse;
import com.exchangediary.member.domain.entity.Member;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class GroupCreateApiTest extends ApiBaseTest {
    private static final String API_PATH = "/api/groups";
    @Autowired
    private GroupRepository groupRepository;

    @Test
    void 그룹_생성_성공() {
        String groupName = "버니즈";
        String profileImage = "red";
        String nickname = "yen";

        var response = RestAssured
                .given().log().all()
                .body(new GroupCreateRequest(groupName, profileImage, nickname))
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post(API_PATH)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(GroupCreateResponse.class);

        Group group = groupRepository.findById(response.groupId()).get();
        assertThat(group.getName()).isEqualTo(groupName);
        Member groupCreator = memberRepository.findById(member.getId()).get();
        assertThat(groupCreator.getGroup().getId()).isEqualTo(group.getId());
        assertThat(groupCreator.getOrderInGroup()).isEqualTo(1);
        assertThat(groupCreator.getNickname()).isEqualTo(nickname);
        assertThat(groupCreator.getProfileImage()).isEqualTo(profileImage);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    "})
    void 그룹_생성_실패_빈_닉네임(String nickname) {
        String groupName = "버니즈";
        String profileImage = "red";

        RestAssured
                .given().log().all()
                .body(new GroupCreateRequest(groupName, profileImage, nickname))
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post(API_PATH)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    "})
    void 그룹_생성_실패_빈_프로필_경로(String profileImage) {
        String groupName = "버니즈";
        String nickname = "yen";

        RestAssured
                .given().log().all()
                .body(new GroupCreateRequest(groupName, profileImage, nickname))
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post(API_PATH)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
