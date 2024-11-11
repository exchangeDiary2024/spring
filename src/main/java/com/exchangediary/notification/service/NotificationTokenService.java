package com.exchangediary.notification.service;

import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.NotFoundException;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.service.MemberQueryService;
import com.exchangediary.notification.domain.NotificationRepository;
import com.exchangediary.notification.domain.entity.Notification;
import com.exchangediary.notification.ui.dto.request.NotificationTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationTokenService {
    private final NotificationRepository notificationRepository;
    private final MemberQueryService memberQueryService;

    @Transactional(readOnly = true)
    public List<String> findTokensByGroup(String groupId) {
        return notificationRepository.findAllTokenByGroupId(groupId);
    }

    @Transactional(readOnly = true)
    public List<String> findTokensByGroupExceptMember(String groupId, Long memberId) {
        return notificationRepository.findAllTokenByGroupIdExceptMemberId(groupId, memberId);
    }

    @Transactional(readOnly = true)
    public List<String> findTokensByCurrentOrderInAllGroup() {
        return notificationRepository.findAllTokenNoDiaryToday();
    }

    @Transactional(readOnly = true)
    public String findTokenByMemberId(Long memberId) {
        return notificationRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.FCM_TOKEN_NOT_FOUND, "", String.valueOf(memberId)))
                .getToken();
    }

    @Transactional(readOnly = true)
    public String findTokenByCurrentOrder(String groupId) {
        return notificationRepository.findByGroupIdAndCurrentOrder(groupId);
    }

    @Transactional(readOnly = true)
    public String findTokenByPreviousOrder(String groupId, int previousOrder) {
        return notificationRepository.findByGroupIdAndOrder(groupId, previousOrder);
    }

    @Transactional
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
