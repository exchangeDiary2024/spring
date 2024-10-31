package com.exchangediary.global.config.web.interceptor;

import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.ForbiddenException;
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
public class GroupAuthorizationInterceptor implements HandlerInterceptor {
    private final MemberQueryService memberQueryService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws IOException {
        Long memberId = (Long) request.getAttribute("memberId");
        Optional<Long> memberGroupId = memberQueryService.findGroupBelongTo(memberId);

        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (!pathVariables.containsKey("groupId")) {
            if (memberGroupId.isEmpty()) {
                return true;
            }
            response.sendRedirect(request.getContextPath()+ "/group/" + memberGroupId.get());
            return false;
        }

        Long groupId = Long.valueOf(String.valueOf(pathVariables.get("groupId")));

        if (memberGroupId.isEmpty()) {
            response.sendRedirect(request.getContextPath()+ "/group");
            return false;
        }

        if (groupId.equals(memberGroupId.get())) {
            request.setAttribute("groupId", groupId);
            return true;
        }

        if (request.getRequestURI().startsWith("/api")) {
            throw new ForbiddenException(
                    ErrorCode.GROUP_FORBIDDEN,
                    "",
                    String.valueOf(groupId)
            );
        }
        response.sendRedirect("/group/" + memberGroupId.get());
        return false;
    }
}
