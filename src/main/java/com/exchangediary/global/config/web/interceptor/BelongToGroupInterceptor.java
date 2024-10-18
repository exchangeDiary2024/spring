package com.exchangediary.global.config.web.interceptor;

import com.exchangediary.member.service.MemberQueryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class BelongToGroupInterceptor implements HandlerInterceptor {
    private final MemberQueryService memberQueryService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws IOException {
        Long memberId = (Long) request.getAttribute("memberId");
        Optional<Long> groupId = memberQueryService.findGroupBelongTo(memberId);

        if (groupId.isEmpty() && !Objects.equals(request.getRequestURI(), "/group")) {
            response.sendRedirect(request.getContextPath()+ "/group");
            return false;
        }
        if (groupId.isPresent() && Objects.equals(request.getRequestURI(), "/group")) {
            response.sendRedirect(request.getContextPath()+ "/group/" + groupId.get());
            return false;
        }
        return true;
    }
}
