package com.exchangediary.notification.domain;

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
    @DisplayName("findAllTokenByGroupId 동작 확인")
    void 전체_그룹원_토큰_가져오기() {
        Group group = Group.of("버니즈", "code");
        entityManager.persist(group);
        createMember(1, group);
        createMember(2, group);
        createMember(3, group);
        createMember(4, group);
        entityManager.flush();

        List<String> tokens = notificationRepository.findAllTokenByGroupId(group.getId());

        assertThat(tokens).hasSize(4);
    }

    @Test
    @DisplayName("findAllTokenByGroupIdExceptMemberId 동작 확인")
    void 본인_제외한_그룹원_토큰_가져오기() {
        Group group = Group.of("버니즈", "code");
        entityManager.persist(group);
        Member self = createMember(1, group);
        createMember(2, group);
        createMember(3, group);
        createMember(4, group);
        entityManager.flush();

        List<String> tokens = notificationRepository.findAllTokenByGroupIdExceptMemberId(group.getId(), self.getId());

        assertThat(tokens).hasSize(3);
    }


    private Member createMember(long num, Group group) {
        Member member = Member.of(num);
        member.joinGroup("one", "red", (int) num, GroupRole.GROUP_MEMBER, group);
        entityManager.persist(member);
        Notification notification = Notification.builder()
                .token(String.valueOf(num))
                .member(member)
                .build();
        entityManager.persist(notification);

        return member;
    }


}


