package com.exchangediary.diary.ui.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DiaryRequest (
        @NotNull String content,
        @NotNull Long todayMoodId
) {
}
