package com.exchangediary.notification.domain;

import com.exchangediary.notification.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByMemberId(Long memberId);
}
