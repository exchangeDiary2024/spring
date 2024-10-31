package com.exchangediary.diary.ui.dto.request;

import com.exchangediary.diary.domain.entity.DiaryContent;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record DiaryRequest (
        @NotBlank(message = "내용을 입력해주세요.") List<DiaryContent> contents,
        @NotBlank(message = "오늘의 기분을 골라주세요.") String moodLocation
) {
}
