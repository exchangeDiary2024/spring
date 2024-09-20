package com.exchangediary.diary.integration;

import com.exchangediary.diary.ui.dto.request.StickerRequest;
import com.exchangediary.diary.ui.dto.response.DiaryDetailResponse;
import com.exchangediary.diary.ui.dto.response.DiaryDetailStickerResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.config.location=classpath:application.yml"})
@Sql(scripts = {"classpath:dummy.sql"})
public class StickerIntegrationTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 스티커_붙이기_성공() {
        double x = 1.0;
        double y = 2.0;
        double width = 10.0;
        double height = 20.0;
        double rotation = 90.0;
        StickerRequest stickerRequest = StickerRequest.builder()
                .coordX(x)
                .coordY(y)
                .width(width)
                .height(height)
                .rotation(rotation)
                .build();

        var response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(stickerRequest)
                .when().post("/diary/" + 1 + "/sticker/" + 1)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        String location = response.header("location");
        assertThat(location).isEqualTo("/diary/" + 1);
        List<DiaryDetailStickerResponse> stickers = RestAssured
                .given().log().all()
                .when().get(location)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(DiaryDetailResponse.class).stickers();
        DiaryDetailStickerResponse savedSticker = stickers.get(stickers.size() - 1);
        assertThat(savedSticker.coordX()).isEqualTo(x);
        assertThat(savedSticker.coordY()).isEqualTo(y);
    }

    @Test
    void 스티커_붙이기_실패_일기없음() {
        StickerRequest stickerRequest = StickerRequest.builder()
                .coordX(1.0)
                .coordY(2.0)
                .width(1.0)
                .height(2.0)
                .rotation(90.0)
                .build();

        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(stickerRequest)
                .when().post("/diary/" + 2 + "/sticker/" + 1)
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void 스티커_붙이기_실패_이미지없음() {
        StickerRequest stickerRequest = StickerRequest.builder()
                .coordX(1.0)
                .coordY(2.0)
                .width(1.0)
                .height(2.0)
                .rotation(90.0)
                .build();

        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(stickerRequest)
                .when().post("/diary/" + 2 + "/sticker/" + 1)
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
