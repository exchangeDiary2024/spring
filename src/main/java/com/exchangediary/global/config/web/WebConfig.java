package com.exchangediary.global.config.web;

import com.exchangediary.global.config.web.interceptor.GroupLeaderAuthorizationInterceptor;
import com.exchangediary.global.config.web.interceptor.GroupAuthorizationInterceptor;
import com.exchangediary.global.config.web.interceptor.JwtAuthenticationInterceptor;
import com.exchangediary.global.config.web.interceptor.LoginInterceptor;
import com.exchangediary.group.service.GroupLeaderService;
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
    private final GroupLeaderService groupLeaderService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthenticationInterceptor(jwtService, cookieService, memberQueryService))
                .addPathPatterns("/group", "/diary/**", "/group/**", "/api/**")
                .excludePathPatterns("/api/kakao/callback");
        registry.addInterceptor(new LoginInterceptor(jwtService, cookieService, memberQueryService))
                .addPathPatterns("/login");
        registry.addInterceptor(new GroupAuthorizationInterceptor(memberQueryService))
                .addPathPatterns("/group/**", "/api/groups/*/**")
                .excludePathPatterns(
                        "/api/groups/*/profile-image",
                        "/api/groups/*/nickname/verify",
                        "/api/groups/*/join",
                        "/api/groups/code/verify"
                );
        registry.addInterceptor(new GroupLeaderAuthorizationInterceptor(groupLeaderService))
                .addPathPatterns("/api/groups/*/leader/**");
    }
}
