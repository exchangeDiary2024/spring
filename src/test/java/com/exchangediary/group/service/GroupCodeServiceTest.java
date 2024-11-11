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
    private GroupQueryService groupQueryService;
    @Autowired
    private GroupRepository groupRepository;

    @Test
    void 그룹_코드_검증_성공() {
        Group group = createGroup();
        groupRepository.save(group);

        String result = groupQueryService.verifyCode(group.getId());

        assertThat(result).isEqualTo(group.getId());
    }

    @Test
    void 그룹_코드_검증_실패() {
        String code = "invalid-code";

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                groupQueryService.verifyCode(code)
        );

        assertThat(exception.getValue()).isEqualTo(code);
    }

    private Group createGroup() {
        return Group.from(GROUP_NAME);
    }
}
