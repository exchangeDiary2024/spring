package com.exchangediary.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final MessageSendService messageSendService;
    private final NotificationTokenService notificationTokenService;

    public void pushNotificationToAllGroupMembers(Long groupId, String title, String body) {
        List<String> tokens = notificationTokenService.findTokensByGroup(groupId);
        messageSendService.sendMulticastMessage(tokens, title, body);
    }
}
