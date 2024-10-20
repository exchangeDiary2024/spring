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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpiredJwtAuthenticationInterceptorTest extends ApiBaseTest {
    @Value("${security.jwt.secret-key}")
    private String secretKey;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

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
