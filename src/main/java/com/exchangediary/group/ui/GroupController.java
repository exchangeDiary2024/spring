package com.exchangediary.group.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {
    @GetMapping
    public String createOrJoinGroup() {
        return "group/group-page";
    }

    @GetMapping("/{groupId}")
    public String showCalendar(Model model, @PathVariable Long groupId) {
        // 인가 거쳤다고 가정
        model.addAttribute("groupId", groupId);
        return "group/group-calendar";
    }
}
