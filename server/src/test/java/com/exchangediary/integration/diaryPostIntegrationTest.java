package com.exchangediary.integration;

import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.ui.dto.request.DiaryRequest;
import com.exchangediary.global.domain.StaticImageRepository;
import com.exchangediary.global.domain.entity.StaticImage;
import com.exchangediary.global.domain.entity.StaticImageType;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.MultiPartSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.File;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class diaryPostIntegrationTest {
    @LocalServerPort
    int port;
    @Autowired
    private DiaryRepository diaryRepository;
    @Autowired
    private StaticImageRepository staticImageRepository;

    @BeforeEach
    void init() {
        RestAssured.port = port;
    }

    @Test
    void 일기작성성공() throws Exception {
        //given
        StaticImage moodImage = StaticImage.builder()
                .url("/exchange/static/1")
                .type(StaticImageType.MOOD)
                .build();
        staticImageRepository.save(moodImage);
        DiaryRequest diaryRequest = diaryRequest();
        MultiPartSpecification data =
                new MultiPartSpecBuilder(diaryRequest, ObjectMapperType.JACKSON_2)
                        .controlName("data")
                        .mimeType(MediaType.APPLICATION_JSON_VALUE)
                        .charset("UTF-8")
                        .build();

        //when
        String responseBody = RestAssured.given()
                .log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart(data)
                .multiPart("file", new File("src/test/resources/test.png"), "image/png")
                .when()
                .post("/diary")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().asString();

        //then
        Long diaryId = Long.valueOf(responseBody.trim());
        assertThat(diaryId).isNotNull();
        Optional<Diary> savedDiary = diaryRepository.findById(diaryId);
        assertThat(savedDiary.isPresent()).isTrue();
        assertThat(savedDiary.get().getContent()).isEqualTo("내용");
        assertThat(savedDiary.get().getMoodImage().getId()).isEqualTo(moodImage.getId());
    }

    private DiaryRequest diaryRequest() {
        return DiaryRequest.builder()
                .content("내용")
                .todayMoodId(1L)
                .build();
    }
}
