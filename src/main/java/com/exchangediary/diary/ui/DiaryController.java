package com.exchangediary.diary.ui;

import com.exchangediary.diary.service.DiaryQueryService;
import com.exchangediary.diary.ui.dto.response.DiaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/group/{groupId}/diary")
public class DiaryController {
    private final DiaryQueryService diaryQueryService;

    @GetMapping
    public String writePage(Model model, @PathVariable Long groupId) {
        model.addAttribute("groupId", groupId);
        return "diary/write-page";
    }

    @GetMapping("/{diaryId}")
    public String viewDiary(
            Model model,
            @PathVariable Long groupId,
            @PathVariable Long diaryId
    ) {
        DiaryResponse diary = diaryQueryService.viewDiary(diaryId);
        model.addAttribute("groupId", groupId);
        model.addAttribute("diary", diary);
        return "diary/view-diary";
    }
}
