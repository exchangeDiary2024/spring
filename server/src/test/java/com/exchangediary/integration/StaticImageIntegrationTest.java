package com.exchangediary.integration;

import com.exchangediary.global.domain.StaticImageRepository;
import com.exchangediary.global.domain.entity.StaticImage;
import com.exchangediary.global.domain.entity.StaticImageType;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StaticImageIntegrationTest {
    @LocalServerPort
    int port;
    @Autowired
    private StaticImageRepository staticImageRepository;

    @BeforeEach
    void init() {
        RestAssured.port = port;
    }

    @Test
    void 기분목록조회_성공() {
        //given
        StaticImage staticImage1 = StaticImage.builder()
                .url("/exchange/static/1")
                .type(StaticImageType.MOOD)
                .build();
        staticImageRepository.save(staticImage1);

        StaticImage staticImage2 = StaticImage.builder()
                .url("/exchange/static/2")
                .type(StaticImageType.MOOD)
                .build();
        staticImageRepository.save(staticImage2);

        //when
        ExtractableResponse<Response> extract = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/moods")
                .then()
                .extract();

        //then
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(extract.jsonPath()).isEqualTo("/exchange/static/1");
    }
}
