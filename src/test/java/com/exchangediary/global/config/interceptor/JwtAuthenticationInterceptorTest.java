package com.exchangediary.global.config.interceptor;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.member.domain.RefreshTokenRepository;
import com.exchangediary.member.domain.entity.RefreshToken;
import com.exchangediary.member.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtAuthenticationInterceptorTest extends ApiBaseTest {
    @Value("${security.jwt.secret-key}")
    private String secretKey;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

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

    @Test
    void 유효한_액세스_토큰으로_인증_성공() {
        RestAssured
                .given().log().all()
                .cookie("token", this.token)
                .redirects().follow(false)
                .when().get("/group")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 액세스_토큰_재발급_후_인증_성공() {
        this.token = buildExpiredAccessToken();
        RefreshToken refreshToken = RefreshToken.of(jwtService.generateRefreshToken(), this.member);
        refreshTokenRepository.save(refreshToken);

        String token = RestAssured
                .given().log().all()
                .cookie("token", this.token)
                .when().get("/group")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .cookie("token");

        Long memberId = jwtService.extractMemberId(token);
        assertThat(memberId).isEqualTo(this.member.getId());
    }

    @Test
    @DisplayName("Expired access token, and no refresh token. Then fail authentication.")
    void 리프레쉬_토큰_없음_인증_실패() {
        this.token = buildExpiredAccessToken();

        RestAssured
                .given().log().all()
                .cookie("token", this.token)
                .redirects().follow(false)
                .when().get("/group")
                .then().log().all()
                .statusCode(HttpStatus.FOUND.value());
    }

    @Test
    void 만료된_리프레쉬_토큰_인증_실패() {
        this.token = buildExpiredAccessToken();
        RefreshToken refreshToken = RefreshToken.of(buildExpiredRefreshToken(), this.member);
        refreshTokenRepository.save(refreshToken);

        RestAssured
                .given().log().all()
                .cookie("token", this.token)
                .redirects().follow(false)
                .when().get("/group")
                .then().log().all()
                .statusCode(HttpStatus.FOUND.value());

        Optional<RefreshToken> result = refreshTokenRepository.findByMemberId(member.getId());
        assertThat(result.isEmpty()).isTrue();
    }

    private String buildExpiredAccessToken() {
        Date now = new Date(System.currentTimeMillis());
        Date expiration = new Date(now.getTime() - 1000);

        return Jwts
                .builder()
                .setSubject(String.valueOf(this.member.getId()))
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String buildExpiredRefreshToken() {
        Date now = new Date(System.currentTimeMillis());
        Date expiration = new Date(now.getTime() - 1000);

        return Jwts
                .builder()
                .setIssuedAt(now)
                .setExpiration(expiration )
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
