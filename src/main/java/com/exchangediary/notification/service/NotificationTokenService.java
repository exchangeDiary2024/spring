package com.exchangediary.notification.service;

import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.service.MemberQueryService;
import com.exchangediary.notification.domain.NotificationRepository;
import com.exchangediary.notification.domain.entity.Notification;
import com.exchangediary.notification.ui.dto.request.NotificationTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationTokenService {
    private final NotificationRepository notificationRepository;
    private final MemberQueryService memberQueryService;

    public void saveNotificationToken(NotificationTokenRequest notificationTokenRequest, Long memberId) {
        Member member = memberQueryService.findMember(memberId);

        notificationRepository.findByMemberId(memberId)
                .ifPresentOrElse(
                        notification -> {
                            notification.updateToken(notificationTokenRequest.token());
                            notificationRepository.save(notification);
                        },
                        () -> {
                            Notification notification = Notification.builder()
                                    .token(notificationTokenRequest.token())
                                    .member(member)
                                    .build();
                            notificationRepository.save(notification);
                        }
                );
    }
}
