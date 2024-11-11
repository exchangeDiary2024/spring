package com.exchangediary.notification.domain;

import com.exchangediary.notification.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByMemberId(Long memberId);
    @Query("SELECT n.token FROM Notification n JOIN n.member m WHERE m.group.id = :groupId")
    List<String> findAllTokenByGroupId(String groupId);
    @Query("SELECT n.token FROM Notification n JOIN n.member m WHERE m.group.id = :groupId AND m.id != :memberId")
    List<String> findAllTokenByGroupIdExceptMemberId(String groupId, Long memberId);
    @Query("SELECT n.token FROM Notification n JOIN n.member m WHERE m.group.id = :groupId AND m.orderInGroup = m.group.currentOrder")
    String findCurrentOrderMemberByGroupId(String groupId);
    @Query("SELECT n.token FROM Notification n " +
            "JOIN n.member m " +
            "JOIN m.group g " +
            "LEFT JOIN Diary d ON d.group = g AND CAST(d.createdAt AS DATE) = CURRENT_DATE " +
            "WHERE m.orderInGroup = m.group.currentOrder AND d.id is NULL")
    List<String> findAllTokenNoDiaryToday();
}
