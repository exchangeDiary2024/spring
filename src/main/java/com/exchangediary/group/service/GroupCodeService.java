package com.exchangediary.group.service;

import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.NotFoundException;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupCodeService {
    private final GroupRepository groupRepository;
    private final GroupQueryService groupQueryService;

    public String generateCode(String groupName) {
        LocalDateTime today = LocalDateTime.now();
        String code = groupName + today + UUID.randomUUID();

        return Base64.getEncoder().encodeToString(code.getBytes());
    }

    public Long verifyCode(String code) {
        Group group = groupRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.GROUP_NOT_FOUND,
                        "",
                        code
                ));

        return group.getId();
    }

    public String findByGroupId(Long groupId) {
        Group group = groupQueryService.findGroup(groupId);
        return group.getCode();
    }
}
