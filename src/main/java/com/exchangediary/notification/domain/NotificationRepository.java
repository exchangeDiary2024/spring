package com.exchangediary.notification.domain;

import com.exchangediary.notification.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByMemberId(Long memberId);
    @Query("SELECT n.token FROM Notification n JOIN n.member m WHERE m.group.id = :groupId")
    List<String> findAllTokenByGroupId(Long groupId);
}
