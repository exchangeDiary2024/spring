package com.exchangediary.group.ui.dto.response;

import com.exchangediary.group.domain.entity.Group;
import lombok.Builder;

import java.time.format.DateTimeFormatter;

@Builder
public record GroupResponse(
        Long id,
        String name,
        String code
) {
    public static GroupResponse of(Group group) {
        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .code(group.getCode())
                .build();
    }
}
