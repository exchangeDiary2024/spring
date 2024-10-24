package com.exchangediary.global.config.web.interceptor;

import com.exchangediary.global.exception.serviceexception.ForbiddenException;
import com.exchangediary.group.service.GroupLeaderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@RequiredArgsConstructor
public class GroupLeaderAuthorizationInterceptor implements HandlerInterceptor {
    private final GroupLeaderService groupLeaderService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws IOException {
        Long memberId = (Long) request.getAttribute("memberId");
        try {
            groupLeaderService.isGroupLeader(memberId);
        } catch (ForbiddenException e) {
            return false;
        }
        return true;
    }
}
