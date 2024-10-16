package com.exchangediary.group.service;

import com.exchangediary.global.exception.serviceexception.NotFoundException;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = BEFORE_TEST_METHOD)
public class GroupCodeServiceTest {
    private static final String GROUP_NAME = "버니즈";
    @Autowired
    private GroupCodeService groupCodeService;
    @Autowired
    private GroupRepository groupRepository;

    @Test
    void 그룹_코드_생성() {
        String encodedCode = groupCodeService.generateCode(GROUP_NAME);

        String code = new String(Base64.getDecoder().decode(encodedCode));

        assertThat(code.substring(0, 3)).isEqualTo(GROUP_NAME);
    }

    @Test
    void 그룹_코드_검증_성공() {
        Group group = createGroup();
        groupRepository.save(group);

        Long result = groupCodeService.verifyCode(group.getCode());

        assertThat(result).isEqualTo(group.getId());
    }

    @Test
    void 그룹_코드_검증_실패() {
        String code = "invalid-code";

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
            groupCodeService.verifyCode(code)
        );

        assertThat(exception.getValue()).isEqualTo(code);
    }

    @Test
    void 그룹_코드_반환_성공() {
        Group group = createGroup();
        groupRepository.save(group);

        String code = groupCodeService.findByGroupId(group.getId());

        assertThat(code).isEqualTo(group.getCode());
    }

    @Test
    void 그룹_코드_반환_실패_그룹존재안함() {
        assertThrows(NotFoundException.class, () -> {
            groupCodeService.findByGroupId(1L);
        });
    }

    private Group createGroup() {
        return Group.builder()
                .name(GROUP_NAME)
                .currentOrder(0)
                .code("code")
                .build();
    }
}
