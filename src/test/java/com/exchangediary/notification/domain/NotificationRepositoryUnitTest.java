package com.exchangediary.notification.domain;

import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.entity.DiaryContent;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.domain.enums.GroupRole;
import com.exchangediary.notification.domain.entity.Notification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class NotificationRepositoryUnitTest {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    @DisplayName("findByMemberId 동작 확인")
    void 멤버의_토큰_여러개_가져오기() {
        Group group = Group.from("버니즈");
        entityManager.persist(group);
        Member self = createMember(1, group);
        entityManager.persist(Notification.builder()
                .token("token-one")
                .member(self)
                .build());
        entityManager.persist(Notification.builder()
                .token("token-two")
                .member(self)
                .build());
        entityManager.flush();

        List<Notification> notifications = notificationRepository.findByMemberId(self.getId());

        assertThat(notifications).hasSize(3);
    }

    @Test
    @DisplayName("findAllTokenByGroupIdExceptMemberId 동작 확인")
    void 본인_제외한_그룹원_토큰_가져오기() {
        Group group = Group.from("버니즈");
        entityManager.persist(group);
        Member self = createMember(1, group);
        createMember(2, group);
        createMember(3, group);
        createMember(4, group);
        entityManager.flush();

        List<String> tokens = notificationRepository.findTokensByGroupIdExceptMemberId(group.getId(), self.getId());

        assertThat(tokens).hasSize(3);
    }

    @Test
    @DisplayName("findAllTokenByGroupIdExceptMemberIdAndLeader 동작 확인")
    void 본인과_방장_제외한_그룹원_토큰_가져오기() {
        Group group = Group.from("버니즈");
        entityManager.persist(group);
        Member self = createMember(1, group);
        createMember(2, group);
        createMember(3, group);
        createMember(4, group);
        createLeader(5, group);
        entityManager.flush();

        List<String> tokens = notificationRepository.findTokensByGroupIdExceptMemberIdAndLeader(group.getId(), self.getId());

        assertThat(tokens).hasSize(3);
    }

    @Test
    @DisplayName("findAllTokenByGroupIdExceptMemberIdAndLeader 동작 확인")
    void 현재_순서_그룹원_토큰_가져오기() {
        Group group = Group.from("버니즈");
        entityManager.persist(group);
        createMember(1, group);
        createMember(2, group);
        createMember(3, group);
        createMember(4, group);
        entityManager.flush();

        List<String> tokens = notificationRepository.findByGroupIdAndCurrentOrder(group.getId());

        assertThat(tokens.get(0)).isEqualTo("버니즈1");
    }

    @Test
    @DisplayName("findAllTokenNoDiaryToday 동작 확인")
    void 오늘_일기_작성하지않은_모든_그룹원_토큰_가져오기() {
        Group group1 = Group.from("그룹1");
        entityManager.persist(group1);
        Member member = createMember(1, group1);
        createDiary(member, group1);
        Group group2 = Group.from("그룹2");
        entityManager.persist(group2);
        createMember(1, group2);
        Group group3 = Group.from("그룹3");
        entityManager.persist(group3);
        createMember(1, group3);
        entityManager.flush();

        List<String> tokens = notificationRepository.findTokensNoDiaryToday();

        assertThat(tokens).hasSize(2);
        assertThat(tokens.contains(group1.getName() + 1)).isFalse();
        assertThat(tokens.contains(group2.getName() + 1)).isTrue();
        assertThat(tokens.contains(group3.getName() + 1)).isTrue();
    }

    private Member createMember(long num, Group group) {
        Member member = Member.of(num);
        member.joinGroup("one", "red", (int) num, GroupRole.GROUP_MEMBER, group);
        entityManager.persist(member);
        Notification notification = Notification.builder()
                .token(group.getName() + num)
                .member(member)
                .build();
        entityManager.persist(notification);

        return member;
    }

    private Member createLeader(long num, Group group) {
        Member member = Member.of(num);
        member.joinGroup("리더", "red", (int) num, GroupRole.GROUP_LEADER, group);
        entityManager.persist(member);
        Notification notification = Notification.builder()
                .token(group.getName() + num)
                .member(member)
                .build();
        entityManager.persist(notification);

        return member;
    }

    private void createDiary(Member member, Group group) {
        Diary diary = Diary.builder()
                .member(member)
                .group(group)
                .todayMood("sad.png")
                .build();
        entityManager.persist(diary);
        DiaryContent diaryContent = DiaryContent.of(1, "content", diary);
        entityManager.persist(diaryContent);
    }
}
