package com.exchangediary.notification.service;

import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.MessagingFailureException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageSendService {
    private static final String TITLE = "스프링";

    public void sendMessage(String token, String body) {
        if (token == null) {
            throw new MessagingFailureException(ErrorCode.NEED_TO_AT_LEAST_ONE_TOKEN, "fcm 토큰이 null입니다.", null);
        }

        Notification notification = Notification.builder()
                .setTitle(TITLE)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new MessagingFailureException(ErrorCode.FAILED_TO_SEND_MESSAGE, "", token);
        }
    }

    public void sendMulticastMessage(List<String> tokens, String body) {
        if (tokens.isEmpty()) {
            throw new MessagingFailureException(ErrorCode.NEED_TO_AT_LEAST_ONE_TOKEN, "", null);
        }

        Notification notification = Notification.builder()
                .setTitle(TITLE)
                .setBody(body)
                .build();

        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(notification)
                .build();

        try {
            FirebaseMessaging.getInstance().sendEachForMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new MessagingFailureException(ErrorCode.FAILED_TO_SEND_MESSAGE, "", tokens.toString());
        }
    }
}
