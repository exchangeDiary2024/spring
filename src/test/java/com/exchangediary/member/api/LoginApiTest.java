package com.exchangediary.member.api;

import com.exchangediary.member.domain.MemberRepository;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.service.JwtService;
import com.exchangediary.member.service.KakaoService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = BEFORE_TEST_METHOD)
public class LoginApiTest {
    @LocalServerPort
    private int port;
    @MockBean
    private KakaoService kakaoService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    void 새로운_회원_카카오_로그인_성공() {
        String mockCode = "randomCode";
        Long mockKakaoId = 1L;

        when(kakaoService.loginKakao(any(String.class))).thenReturn(mockKakaoId);

        var response = RestAssured
                .given().log().all()
                .redirects().follow(false)
                .when().get("/api/kakao/callback?code="+mockCode)
                .then().log().all()
                .statusCode(HttpStatus.FOUND.value())
                .extract();

        String token = response.cookie("token");
        assertThat(token).isNotNull();
    }

    @Test
    void 기존_회원_카카오_로그인_성공() {
        String mockCode = "randomCode";
        Long mockKakaoId = 1L;
        Member member = Member.builder().kakaoId(mockKakaoId).build();
        memberRepository.save(member);

        when(kakaoService.loginKakao(any(String.class))).thenReturn(mockKakaoId);

        var response = RestAssured
                .given().log().all()
                .redirects().follow(false)
                .when().get("/api/kakao/callback?code="+mockCode)
                .then().log().all()
                .statusCode(HttpStatus.FOUND.value())
                .extract();

        String token = response.cookie("token");
        Long memberId = jwtService.extractMemberId(token);
        assertThat(memberId).isEqualTo(member.getId());
    }
}
