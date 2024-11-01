package com.exchangediary.diary.service;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.ui.dto.response.DiaryResponse;
import com.exchangediary.global.exception.serviceexception.NotFoundException;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = BEFORE_TEST_METHOD)
public class DiaryQueryServiceTest extends ApiBaseTest {
    @Autowired
    private DiaryQueryService diaryQueryService;
    @Autowired
    private GroupRepository groupRepository;

    @Test
    void 일기_조회_실패_일기_없음() {
        Group group = createGroup();
        groupRepository.save(group);
        Long diaryId = 1L;

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                diaryQueryService.viewDiary(diaryId)
        );

        assertThat(exception.getValue()).isEqualTo(diaryId.toString());
    }

    private Group createGroup() {
        return Group.of("버니즈", "code");
    }

    private Diary createDiary(Member member, Group group) {
        return Diary.builder()
                .moodLocation("/images/write-page/emoji/sleepy.svg")
                .group(group)
                .member(member)
                .build();
    }
}
