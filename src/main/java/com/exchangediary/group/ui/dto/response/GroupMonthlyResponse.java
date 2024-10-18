package com.exchangediary.group.ui.dto.response;

import com.exchangediary.group.domain.entity.Group;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GroupMonthlyResponse(
        Long id,
        String name,
        LocalDateTime createdAt
) {
    public static GroupMonthlyResponse of(Group group) {
        return GroupMonthlyResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .createdAt(group.getCreatedAt())
                .build();
    }
}
