package com.exchangediary.reply.ui;

import com.exchangediary.reply.service.ReplyCreateService;
import com.exchangediary.reply.ui.dto.request.ReplyCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups/{groupId}/diaries/{diaryId}/comments/{commentId}/replies")
public class ApiReplyController {
    private final ReplyCreateService replyCreateService;

    @PostMapping
    public ResponseEntity<Void> createReply(
            @RequestBody @Valid ReplyCreateRequest request,
            @PathVariable Long diaryId,
            @PathVariable Long commentId,
            @RequestAttribute Long memberId
    ) {
        replyCreateService.createReply(request, diaryId, commentId, memberId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
