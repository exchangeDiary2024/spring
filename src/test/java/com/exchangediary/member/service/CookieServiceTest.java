package com.exchangediary.member.service;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CookieServiceTest {
    @Autowired
    private CookieService cookieService;

    @Test
    void 쿠키_생성() {
        String name = "name";
        String value = "value";

        Cookie cookie = cookieService.createCookie(name, value);

        assertThat(cookie.getName()).isEqualTo(name);
        assertThat(cookie.getValue()).isEqualTo(value);
    }

    @Test
    void 쿠키_값_추출() {
        Cookie[] cookies = {
                cookieService.createCookie("name1", "value1"),
                cookieService.createCookie("name2", "value2"),
                cookieService.createCookie("name3", "value3"),
        };

        String value = cookieService.getValueFromCookies(cookies, "name2");

        assertThat(value).isEqualTo("value2");
    }
}
