package com.exchangediary.group.ui;

import com.exchangediary.group.service.GroupQueryService;
import com.exchangediary.group.ui.dto.response.GroupResponse;
import com.exchangediary.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {
    private final GroupQueryService groupQueryService;
    private final MemberQueryService memberQueryService;

    @GetMapping("")
    public String createOrJoinGroup(
            @RequestAttribute Long memberId
    ) {
        if (memberQueryService.isInGroup(memberId)) {
            return "redirect:/";
        }
        return "group/group-page";
    }

    @GetMapping("/{groupId}")
    public String showMonthlyPage(
            Model model,
            @PathVariable Long groupId
    ) {
        GroupResponse group = groupQueryService.viewGroup(groupId);
        model.addAttribute("group", group);
        return "group/monthly-page";
    }
}
