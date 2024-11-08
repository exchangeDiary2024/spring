package com.exchangediary.notification.config;

import com.exchangediary.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 9,21 * * *")
    public void pushWriteDiaryNotification() {
        notificationService.pushWriteDiaryNotification();
    }
}
