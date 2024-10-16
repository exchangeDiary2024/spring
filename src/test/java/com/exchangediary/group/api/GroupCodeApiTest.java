package com.exchangediary.group.api;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.group.ui.dto.request.GroupCodeRequest;
import com.exchangediary.group.ui.dto.response.GroupIdResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class GroupCodeApiTest extends ApiBaseTest {
    private static final String API_PATH = "/api/groups/code/verify";
    private static final String GROUP_NAME = "버니즈";
    @Autowired
    private GroupRepository groupRepository;

    @Test
    void 그룹_코드_유효성_검증_성공() {
        Group group = createGroup();
        groupRepository.save(group);
        GroupCodeRequest groupCodeRequest = new GroupCodeRequest(group.getCode());

        GroupIdResponse response = RestAssured
                .given().log().all()
                .body(groupCodeRequest)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post(API_PATH)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(GroupIdResponse.class);

        assertThat(response.groupId()).isEqualTo(group.getId());
    }

    @Test
    void 그룹_코드_유효성_검증_실패() {
        createGroup();
        GroupCodeRequest groupCodeRequest = new GroupCodeRequest("invalid-code");

        RestAssured
                .given().log().all()
                .body(groupCodeRequest)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post(API_PATH)
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void 그룹_코드_유효성_검증_실패_빈코드() {
        GroupCodeRequest groupCodeRequest = new GroupCodeRequest("");

        RestAssured
                .given().log().all()
                .body(groupCodeRequest)
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post(API_PATH)
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
}
