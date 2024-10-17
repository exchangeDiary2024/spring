package com.exchangediary.global.config.web.interceptor;

import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.UnauthorizedException;
import com.exchangediary.member.service.CookieService;
import com.exchangediary.member.service.JwtService;
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

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws IOException {
        try {
            String token = getJwtTokenFromCookies(request);
            jwtService.verifyToken(token);

            Long memberId = jwtService.extractMemberId(token);
            request.setAttribute("memberId", memberId);
        } catch (UnauthorizedException exception) {
            return true;
        }
        response.sendRedirect(request.getContextPath()+ "/group");
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
}
