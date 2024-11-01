package com.exchangediary.diary.domain;

import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.group.domain.GroupRepository;
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
    @Autowired
    private GroupRepository groupRepository;

    @Test
    void 오늘_일기_있는_경우_조회() {
        Group group = Group.of("group-name", "code");
        Member member = Member.of(1234L);
        Diary diary = Diary.builder()
                .moodLocation("/images/write-page/emoji/sleepy.svg")
                .member(member)
                .group(group)
                .build();
        entityManager.persist(group);
        entityManager.persist(member);
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
        Group group = Group.of("group-name", "code");
        entityManager.persist(group);
        Member diaryCreator = Member.of(1235L);
        entityManager.persist(diaryCreator);
        Diary diary = Diary.builder()
                .moodLocation("/images/write-page/emoji/sleepy.svg")
                .member(diaryCreator)
                .group(group)
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
        Group group = Group.of("group-name", "code");
        entityManager.persist(group);
        Member diaryCreator = Member.of(1235L);
        entityManager.persist(diaryCreator);
        Diary diary = Diary.builder()
                .moodLocation("/images/write-page/emoji/sleepy.svg")
                .member(diaryCreator)
                .group(group)
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
