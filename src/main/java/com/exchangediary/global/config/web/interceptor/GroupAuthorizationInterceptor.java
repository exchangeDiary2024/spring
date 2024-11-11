package com.exchangediary.global.config.web.interceptor;

import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.ForbiddenException;
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
        Optional<String> memberGroupId = memberQueryService.findGroupBelongTo(memberId);

        handleNotExistRequestUrl(pathVariables, request.getRequestURI());
        if (isGroupCreatePageRequest(pathVariables)) {
            return processGroupCreatePageRequest(memberGroupId.orElse(null), response);
        }

        String groupId = String.valueOf(pathVariables.get("groupId"));
        if (memberGroupId.isEmpty()) {
            response.sendRedirect("/groups");
            return false;
        }
        return processGroupAuthorization(groupId, memberGroupId.get(), request);
    }

    private void handleNotExistRequestUrl(Map<String, String> pathVariables, String url) {
        if (pathVariables == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND, "", url);
        }
    }

    private boolean isGroupCreatePageRequest(Map<String, String> pathVariables) {
        return !pathVariables.containsKey("groupId");
    }

    private boolean processGroupCreatePageRequest(String memberGroupId, HttpServletResponse response) throws IOException {
        if (memberGroupId == null) {
            return true;
        }
        response.sendRedirect("/groups/" + memberGroupId);
        return false;
    }

    private boolean processGroupAuthorization(
            String groupId,
            String memberGroupId,
            HttpServletRequest request
    ) {
        if (!groupId.equals(memberGroupId)) {
            throw new ForbiddenException(
                    ErrorCode.GROUP_FORBIDDEN,
                    "",
                    request.getRequestURI()
            );
        }

        request.setAttribute("groupId", groupId);
        return true;
    }
}
