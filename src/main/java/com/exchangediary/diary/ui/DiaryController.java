package com.exchangediary.diary.ui;

import com.exchangediary.diary.service.DiaryAuthorizationService;
import com.exchangediary.diary.service.DiaryQueryService;
import com.exchangediary.diary.ui.dto.response.DiaryTopResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/groups/{groupId}/diaries")
public class DiaryController {
    private final DiaryAuthorizationService diaryAuthorizationService;
    private final DiaryQueryService diaryQueryService;

    @GetMapping
    public String writePage(
            Model model,
            @PathVariable Long groupId,
            @RequestAttribute Long memberId
    ) {
        diaryAuthorizationService.checkDiaryWritable(groupId, memberId);
        model.addAttribute("groupId", groupId);
        return "diary/write-page";
    }

    @GetMapping("/{diaryId}")
    public String viewDiary(
            Model model,
            @PathVariable Long groupId,
            @PathVariable Long diaryId,
            @RequestAttribute Long memberId
    ) {
        DiaryTopResponse diary = diaryQueryService.viewDiaryTop(memberId, diaryId);
        model.addAttribute("groupId", groupId);
        model.addAttribute("diary", diary);
        return "diary/view-page";
    }
}
