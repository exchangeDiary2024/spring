package com.exchangediary.diary.api;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.ui.dto.response.DiaryIdResponse;
import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

class DiaryFindIdApiTest extends ApiBaseTest {
    private static final String API_PATH = "/api/groups/%d/diaries";
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private DiaryRepository diaryRepository;

    @Test
    void 일기_id_조회_성공() {
        Group group = createGroup();
        groupRepository.save(group);
        Diary diary = createDiary(group);
        diaryRepository.save(diary);
        Long diaryId = RestAssured
                .given().log().all()
                .queryParam("year", diary.getCreatedAt().getYear())
                .queryParam("month", diary.getCreatedAt().getMonth().getValue())
                .queryParam("day", diary.getCreatedAt().getDayOfMonth())
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get(String.format(API_PATH, group.getId()))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(DiaryIdResponse.class)
                .diaryId();

        assertThat(diaryId).isEqualTo(diary.getId());
    }

    @Test
    void 일기_id_조회_실패_일기_없음() {
        Group group = createGroup();
        groupRepository.save(group);
        RestAssured
                .given().log().all()
                .queryParam("year", "2024")
                .queryParam("month", "10")
                .queryParam("day", "12")
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get(String.format(API_PATH, group.getId()))
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo(ErrorCode.DIARY_NOT_FOUND.getMessage()));
    }

    @Test
    void 일기_id_조회_실패_빈_형식() {
        Group group = createGroup();
        groupRepository.save(group);
        RestAssured
                .given().log().all()
                .queryParam("year", "2024")
                .queryParam("month", "10")
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get(String.format(API_PATH, group.getId()))
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 일기_id_조회_실패_빈_값() {
        Group group = createGroup();
        groupRepository.save(group);
        RestAssured
                .given().log().all()
                .queryParam("year", "")
                .queryParam("month", "10")
                .queryParam("day", "10")
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get(String.format(API_PATH, group.getId()))
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 일기_id_조회_실패_날짜_유효성_검사() {
        Group group = createGroup();
        groupRepository.save(group);
        RestAssured
                .given().log().all()
                .queryParam("year", "2024")
                .queryParam("month", "13")
                .queryParam("day", "10")
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get(String.format(API_PATH, group.getId()))
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private Group createGroup() {
        return Group.builder()
                .name("버니즈")
                .currentOrder(0)
                .code("code")
                .build();
    }

    private Diary createDiary(Group group) {
        return Diary.builder()
                .content("내용")
                .moodLocation("/images/write-page/emoji/sleepy.svg")
                .group(group)
                .member(member)
                .build();
    }

}
