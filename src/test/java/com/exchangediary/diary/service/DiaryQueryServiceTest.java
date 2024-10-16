package com.exchangediary.diary.service;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.ui.dto.response.DiaryResponse;
import com.exchangediary.global.exception.serviceexception.NotFoundException;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.MemberRepository;
import com.exchangediary.member.domain.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = BEFORE_TEST_METHOD)
public class DiaryQueryServiceTest extends ApiBaseTest {
    private static final String GROUP_NAME = "버니즈";
    @Autowired
    private DiaryQueryService diaryQueryService;
    @Autowired
    private DiaryRepository diaryRepository;
    @Autowired
    private GroupRepository groupRepository;

    @Test
    void 일기_조회_성공() {
        Group group = createGroup();
        groupRepository.save(group);
        Diary diary = createDiary(member, group);
        diaryRepository.save(diary);
        DiaryResponse response = diaryQueryService.viewDiary(diary.getId());

        assertThat(response.diaryId()).isEqualTo(diary.getId());
    }

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
        return Group.builder()
                .name(GROUP_NAME)
                .currentOrder(0)
                .code("code")
                .build();
    }

    private Diary createDiary(Member member, Group group) {
        return Diary.builder()
                .content("내용")
                .moodLocation("/images/write-page/emoji/sleepy.svg")
                .group(group)
                .member(member)
                .build();
    }
}
