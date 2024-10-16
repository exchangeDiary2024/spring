package com.exchangediary.member.ui;

import com.exchangediary.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberQueryService memberQueryService;

    @GetMapping("/")
    public String home(@RequestAttribute Long memberId) {
        if (!memberQueryService.isInGroup(memberId)) {
            return "redirect:/group";
        }
        return "index.html";
    }
}
