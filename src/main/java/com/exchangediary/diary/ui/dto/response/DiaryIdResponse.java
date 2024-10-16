package com.exchangediary.diary.ui.dto.response;

import lombok.Builder;

@Builder
public record DiaryIdResponse(
        Long diaryId
) {
}
