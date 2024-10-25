package com.exchangediary.global.config.web.interceptor;

import com.exchangediary.global.exception.serviceexception.NotFoundException;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.group.service.GroupMemberService;
import com.exchangediary.group.service.GroupQueryService;
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
    private final GroupMemberService groupMemberService;
    private final GroupQueryService groupQueryService;
    private final MemberQueryService memberQueryService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws IOException {
        Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long groupId = Long.valueOf(String.valueOf(pathVariables.get("groupId")));
        Group group = groupQueryService.findGroup(groupId);
        Long memberId = (Long) request.getAttribute("memberId");

        try {
            groupMemberService.findSelfInGroup(group, memberId);
            request.setAttribute("groupId", groupId);
        } catch (NotFoundException exception) {
            if (request.getRequestURI().startsWith("/group")) {
                Optional<Long> authorizedGroupId = memberQueryService.findGroupBelongTo(memberId);
                String updatedUri = request.getRequestURI()
                        .replace("/group/" + groupId, "/group/" + authorizedGroupId);
                response.sendRedirect(updatedUri);
            }
            return false;
        }
        return true;
    }
}
