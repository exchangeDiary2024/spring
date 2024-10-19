package com.exchangediary.global.config.interceptor;

import com.exchangediary.ApiBaseTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JwtAuthenticationInterceptorTest extends ApiBaseTest {
    @Test
    void 쿠키에_토큰없는_경우_인증_실패() {
        RestAssured
                .given().log().all()
                .redirects().follow(false)
                .when().get("/group")
                .then().log().all()
                .statusCode(HttpStatus.FOUND.value());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "invalid-token"})
    void 쿠키에_잘못된_토큰_들어가는_경우_인증_실패(String token) {
        RestAssured
                .given().log().all()
                .cookie("token", token)
                .redirects().follow(false)
                .when().get("/group")
                .then().log().all()
                .statusCode(HttpStatus.FOUND.value());
    }
}
