package com.exchangediary.group.ui.dto.response;

import com.exchangediary.group.domain.entity.Group;
import lombok.Builder;

@Builder
public record GroupCreateResponse(
        Long groupId,
        String code
) {
    public static GroupCreateResponse of(Group group) {
        return GroupCreateResponse.builder()
                .groupId(group.getId())
                .code(group.getCode())
                .build();
    }
}
