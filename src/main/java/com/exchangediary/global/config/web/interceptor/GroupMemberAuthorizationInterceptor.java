package com.exchangediary.global.config.web.interceptor;

import com.exchangediary.global.exception.serviceexception.NotFoundException;
import com.exchangediary.member.service.MemberQueryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class GroupMemberAuthorizationInterceptor implements HandlerInterceptor {
    private final MemberQueryService memberQueryService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws IOException {
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long groupId = Long.valueOf(String.valueOf(pathVariables.get("groupId")));
        Long memberId = (Long) request.getAttribute("memberId");

        try {
            memberQueryService.checkMemberOfGroup(memberId, groupId);
            request.setAttribute("groupId", groupId);
        } catch (NotFoundException exception) {
            if (request.getRequestURI().startsWith("/api")) {
                throw exception;
            }
            Optional<Long> authorizedGroupId = memberQueryService.findGroupBelongTo(memberId);
            response.sendRedirect("/group/" + authorizedGroupId.get());
            return false;
        }
        return true;
    }
}
