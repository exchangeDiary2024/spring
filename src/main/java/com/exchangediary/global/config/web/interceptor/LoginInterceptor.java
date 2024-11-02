package com.exchangediary.global.config.web.interceptor;

import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.UnauthorizedException;
import com.exchangediary.member.service.CookieService;
import com.exchangediary.member.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {
    private static final String COOKIE_NAME = "token";

    private final JwtService jwtService;
    private final CookieService cookieService;

    private String token;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws IOException {
        try {
            token = getJwtTokenFromCookies(request);
            verifyAndReissueAccessToken(response);

            Long memberId = jwtService.extractMemberId(token);
            request.setAttribute("memberId", memberId);
        } catch (UnauthorizedException exception) {
            return true;
        }
        response.sendRedirect(request.getContextPath()+ "/groups");
        return false;
    }

    private String getJwtTokenFromCookies(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();

            return cookieService.getValueFromCookies(cookies, COOKIE_NAME);
        } catch (RuntimeException exception) {
            throw new UnauthorizedException(
                    ErrorCode.NEED_TO_REQUEST_TOKEN,
                    "",
                    COOKIE_NAME
            );
        }
    }

    private void verifyAndReissueAccessToken(HttpServletResponse response) {
        try {
            jwtService.verifyAccessToken(token);
        } catch (ExpiredJwtException exception) {
            Long memberId = Long.valueOf(exception.getClaims().getSubject());

            jwtService.verifyRefreshToken(memberId);
            token = jwtService.generateAccessToken(memberId);
            Cookie cookie = cookieService.createCookie(COOKIE_NAME, token);
            response.addCookie(cookie);
        }
    }
}
