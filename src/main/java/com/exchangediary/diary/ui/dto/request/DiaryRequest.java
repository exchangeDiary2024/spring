package com.exchangediary.diary.ui.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DiaryRequest (
        @NotBlank(message = "내용을 입력해주세요.") String content,
        @NotBlank(message = "오늘의 기분을 골라주세요.") String moodLocation
) {
}
