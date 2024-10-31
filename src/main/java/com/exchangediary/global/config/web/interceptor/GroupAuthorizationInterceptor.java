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
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long memberId = (Long) request.getAttribute("memberId");
        Optional<Long> memberGroupId = memberQueryService.findGroupBelongTo(memberId);

        if (isGroupCreatePageRequest(pathVariables)) {
            return processGroupCreatePageRequest(memberGroupId.orElse(null), response);
        }

        Long groupId = Long.valueOf(String.valueOf(pathVariables.get("groupId")));
        if (memberGroupId.isEmpty()) {
            response.sendRedirect("/group");
            return false;
        }
        return processGroupAuthorization(groupId, memberGroupId.get(), request, response);
    }

    private boolean isGroupCreatePageRequest(Map<String, String> pathVariables) {
        return !pathVariables.containsKey("groupId");
    }

    private boolean processGroupCreatePageRequest(Long memberGroupId, HttpServletResponse response) throws IOException {
        if (memberGroupId == null) {
            return true;
        }
        response.sendRedirect("/group/" + memberGroupId);
        return false;
    }

    private boolean processGroupAuthorization(
            Long groupId,
            Long memberGroupId,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        if (groupId.equals(memberGroupId)) {
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
        response.sendRedirect("/group/" + memberGroupId);
        return false;
    }
}
