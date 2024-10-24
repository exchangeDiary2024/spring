package com.exchangediary.member.service;

import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.UnauthorizedException;
import com.exchangediary.member.domain.RefreshTokenRepository;
import com.exchangediary.member.domain.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional(readOnly = true)
    public RefreshToken findRefreshTokenByMemberId(Long memberId) {
        return refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UnauthorizedException(
                        ErrorCode.JWT_TOKEN_UNAUTHORIZED,
                        "",
                        String.valueOf(memberId)
                ));
    }

    public void expireRefreshToken(RefreshToken refreshToken) throws UnauthorizedException {
        refreshTokenRepository.delete(refreshToken);
        throw new UnauthorizedException(ErrorCode.EXPIRED_TOKEN,
                "",
                refreshToken.getToken()
        );
    }
}
