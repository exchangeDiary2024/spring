package com.exchangediary.group.ui;

import com.exchangediary.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

@Controller
@RequiredArgsConstructor
public class GroupController {
    private final MemberQueryService memberQueryService;

    @GetMapping("/group")
    public String createOrJoinGroup(
            @RequestAttribute Long memberId
    ) {
        if (memberQueryService.isInGroup(memberId)) {
            return "redirect:/";
        }
        return "group-page";
    }
}
