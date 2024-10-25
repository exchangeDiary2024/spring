package com.exchangediary.global.config.web.interceptor;

import com.exchangediary.global.exception.serviceexception.ForbiddenException;
import com.exchangediary.group.service.GroupLeaderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class GroupLeaderAuthorizationInterceptor implements HandlerInterceptor {
    private final GroupLeaderService groupLeaderService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        Long memberId = (Long) request.getAttribute("memberId");
        groupLeaderService.isGroupLeader(memberId);
        return true;
    }
}
