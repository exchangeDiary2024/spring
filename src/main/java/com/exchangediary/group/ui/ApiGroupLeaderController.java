package com.exchangediary.group.ui;

import com.exchangediary.group.service.GroupLeaderService;
import com.exchangediary.group.ui.dto.request.GroupKickOutRequest;
import com.exchangediary.group.ui.dto.request.GroupLeaderHandOverRequest;
import com.exchangediary.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/groups/{groupId}/leader")
@RequiredArgsConstructor
public class ApiGroupLeaderController {
    private final GroupLeaderService groupLeaderService;
    private final NotificationService notificationService;

    @PatchMapping("/hand-over")
    public ResponseEntity<Void> handOverGroupLeader(
            @PathVariable String groupId,
            @RequestAttribute Long memberId,
            @RequestBody GroupLeaderHandOverRequest request
    ) {
        groupLeaderService.handOverGroupLeader(groupId, memberId, request);
        notificationService.pushToAllGroupMembersExceptMember(groupId, memberId, "방장이 다른 친구에게 방장 역할을 넘겨줬어요!");
        return ResponseEntity
                .ok()
                .build();
    }

    @PatchMapping("/skip-order")
    public ResponseEntity<Void> skipDiaryOrder(@PathVariable String groupId) {
        int previousOrder = groupLeaderService.skipDiaryOrder(groupId);
        notificationService.pushSkipOverDiaryNotification(groupId, previousOrder);
        notificationService.pushDiaryOrderNotification(groupId);
        return ResponseEntity
                .ok()
                .build();
    }

    @PatchMapping("/leave")
    public ResponseEntity<Void> kickOutMember(
            @PathVariable String groupId,
            @RequestBody GroupKickOutRequest request
    ) {
        long memberId = groupLeaderService.kickOutMember(groupId, request);
        notificationService.pushToAllGroupMembersExceptMember(groupId, memberId, "친구가 그룹에서 나갔어요!");
        notificationService.pushNotification(memberId, "앗, 그룹에서 내보내졌어요.\n다른 스프링에서 일기 쓰기를 시작해요!");
        return ResponseEntity
                .ok()
                .build();
    }
}
