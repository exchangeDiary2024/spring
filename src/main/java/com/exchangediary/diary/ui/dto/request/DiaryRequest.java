package com.exchangediary.diary.ui.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DiaryRequest (
        @NotBlank String content,
        @NotBlank String moodLocation
) {
}
