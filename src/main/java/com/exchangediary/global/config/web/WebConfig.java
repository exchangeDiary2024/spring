package com.exchangediary.global.config.web;

import com.exchangediary.global.config.web.interceptor.JwtAuthenticationInterceptor;
import com.exchangediary.member.service.CookieService;
import com.exchangediary.member.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final JwtService jwtService;
    private final CookieService cookieService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthenticationInterceptor(jwtService, cookieService))
                .addPathPatterns("/", "/diary/**", "/group/**", "/api/**")
                .excludePathPatterns("/api/kakao/callback");
    }
}
