package com.exchangediary.member.service;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"security.jwt.access-token.expiration-time=1000"})
public class ExpiredJwtTest {
    @Autowired
    private JwtService jwtService;

    @Test
    void 만료된_토큰_검증() throws InterruptedException {
        Long memberId = 1L;
        String token = jwtService.generateAccessToken(memberId);

        Thread.sleep(1000);
        assertThrows(ExpiredJwtException.class, () ->
                jwtService.verifyAccessToken(token)
        );
    }
}
