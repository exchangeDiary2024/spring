package com.exchangediary.global.config.interceptor;

import com.exchangediary.ApiBaseTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginInterceptorTest extends ApiBaseTest {
    @Test
    void 토큰_발급받은_사용자가_로그인_페이지_접근() {
        String location = RestAssured
                .given().log().all()
                .cookie("token", token)
                .redirects().follow(false)
                .when().get("/login")
                .then()
                .log().status()
                .log().headers()
                .statusCode(HttpStatus.FOUND.value())
                .extract()
                .header("location");

        assertThat(location.substring(location.indexOf("/groups"))).isEqualTo("/groups");
    }

    @Test
    void 토큰_발급받지않은_사용자가_로그인_페이지_접근() {
        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get("/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
