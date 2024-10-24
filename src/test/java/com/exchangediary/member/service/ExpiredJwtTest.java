package com.exchangediary.member.service;

import com.exchangediary.global.exception.serviceexception.UnauthorizedException;
import com.exchangediary.member.domain.MemberRepository;
import com.exchangediary.member.domain.RefreshTokenRepository;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.domain.entity.RefreshToken;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "security.jwt.access-token.expiration-time=1000",
        "security.jwt.refresh-token.expiration-time=1000"
})
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = BEFORE_TEST_METHOD)
public class ExpiredJwtTest {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void 만료된_액세스_토큰_검증() throws InterruptedException {
        Long memberId = 1L;
        String token = jwtService.generateAccessToken(memberId);

        Thread.sleep(1000);
        assertThrows(ExpiredJwtException.class, () ->
                jwtService.verifyAccessToken(token)
        );
    }

    @Test
    void 만료된_리프레쉬_토큰_검증() throws InterruptedException {
        Member member = Member.from(1L);
        memberRepository.save(member);
        RefreshToken refreshToken = RefreshToken.of(
                jwtService.generateRefreshToken(),
                member
        );
        refreshTokenRepository.save(refreshToken);

        Thread.sleep(1000);
        assertThrows(UnauthorizedException.class, () ->
                jwtService.verifyRefreshToken(member.getId())
        );

        Optional<RefreshToken> result = refreshTokenRepository.findByMemberId(member.getId());
        assertThat(result.isEmpty()).isTrue();
    }
}
