package com.exchangediary.member.service;

import com.exchangediary.global.exception.serviceexception.UnauthorizedException;
import com.exchangediary.member.domain.MemberRepository;
import com.exchangediary.member.domain.RefreshTokenRepository;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.domain.entity.RefreshToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RefreshTokenVerifyTest {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void 리프레쉬_토큰_검증_성공() {
        Member member = Member.from(1L);
        memberRepository.save(member);
        RefreshToken refreshToken = RefreshToken.of(
                jwtService.generateRefreshToken(),
                member
        );
        refreshTokenRepository.save(refreshToken);

        jwtService.verifyRefreshToken(member.getId());
    }


    @Test
    void 사용자_id와_일치하는_리프레쉬_토큰_없음() {
        Long memberId = 1L;

        assertThrows(UnauthorizedException.class, () ->
                jwtService.verifyRefreshToken(memberId)
        );
    }
}
