package com.exchangediary.global.config.web;

import com.exchangediary.global.config.web.interceptor.BelongToGroupInterceptor;
import com.exchangediary.global.config.web.interceptor.JwtAuthenticationInterceptor;
import com.exchangediary.global.config.web.interceptor.LoginInterceptor;
import com.exchangediary.member.service.CookieService;
import com.exchangediary.member.service.JwtService;
import com.exchangediary.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final MemberQueryService memberQueryService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthenticationInterceptor(jwtService, cookieService))
                .addPathPatterns("/group", "/diary/**", "/group/**", "/api/**")
                .excludePathPatterns("/api/kakao/callback");
        registry.addInterceptor(new BelongToGroupInterceptor(memberQueryService))
                .addPathPatterns("/group/{*groupId}");
        registry.addInterceptor(new LoginInterceptor(jwtService, cookieService))
                .addPathPatterns("/login");
    }
}
