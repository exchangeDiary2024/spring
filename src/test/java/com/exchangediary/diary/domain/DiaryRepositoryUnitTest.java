package com.exchangediary.diary.domain;

import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.MemberRepository;
import com.exchangediary.member.domain.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DiaryRepositoryUnitTest {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private DiaryRepository diaryRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 오늘_일기_있는_경우_조회() {
        Group group = Group.of("group-name", "code");
        Diary diary = Diary.builder()
                .moodLocation("/images/write-page/emoji/sleepy.svg")
                .group(group)
                .build();
        entityManager.persist(group);
        entityManager.persist(diary);

        Optional<Diary> result = diaryRepository.findTodayDiaryInGroup(group.getId());

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void 오늘_일기_없는_경우_조회() {
        Group group = Group.of("group-name", "code");
        entityManager.persist(group);

        Optional<Diary> result = diaryRepository.findTodayDiaryInGroup(group.getId());

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void 일기_조회_가능() {
        Diary diary = Diary.builder()
                .moodLocation("/images/write-page/emoji/sleepy.svg")
                .build();
        diaryRepository.save(diary);
        Member member = Member.builder()
                .kakaoId(1234L)
                .lastViewableDiaryDate(LocalDate.now())
                .build();
        memberRepository.save(member);

        boolean result = diaryRepository.isViewableDiary(member.getId(), diary.getId());

        assertThat(result).isTrue();
    }

    @Test
    void 일기_조회_불가능() {
        Diary diary = Diary.builder()
                .moodLocation("/images/write-page/emoji/sleepy.svg")
                .build();
        diaryRepository.save(diary);
        Member member = Member.builder()
                .kakaoId(1234L)
                .lastViewableDiaryDate(LocalDate.now().minusMonths(1))
                .build();
        memberRepository.save(member);

        boolean result = diaryRepository.isViewableDiary(member.getId(), diary.getId());

        assertThat(result).isFalse();
    }
}
