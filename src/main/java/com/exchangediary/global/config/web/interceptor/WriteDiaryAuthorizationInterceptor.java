package com.exchangediary.global.config.web.interceptor;

import com.exchangediary.diary.service.DiaryAuthorizationService;
import com.exchangediary.global.exception.serviceexception.ForbiddenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class WriteDiaryAuthorizationInterceptor implements HandlerInterceptor {
    private final DiaryAuthorizationService diaryAuthorizationService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws IOException {
        if (!request.getParameterMap().isEmpty()) {
            return true;
        }

        Long memberId = (Long) request.getAttribute("memberId");
//        Long groupId = (Long) request.getAttribute("groupId"); todo: 그룹 인가 구현 후 주석 제거 & 아래 코드 제거
        Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long groupId = Long.valueOf(String.valueOf(pathVariables.get("groupId")));

        try {
            return diaryAuthorizationService.canWriteDiary(memberId, groupId);
        } catch (ForbiddenException exception) {
            if (request.getRequestURI().contains("/api")) {
                throw exception;
            }
            response.sendRedirect("/group/" + groupId);
            return false;
        }
    }
}
