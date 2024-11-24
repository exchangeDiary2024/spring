package com.exchangediary.comment.ui;

import com.exchangediary.comment.service.CommentQueryService;
import com.exchangediary.comment.service.CommentService;
import com.exchangediary.comment.service.ReplyCreateService;
import com.exchangediary.comment.ui.dto.request.CommentCreateRequest;
import com.exchangediary.comment.ui.dto.request.ReplyCreateRequest;
import com.exchangediary.comment.ui.dto.response.CommentCreateResponse;
import com.exchangediary.comment.ui.dto.response.CommentCreationVerifyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups/{groupId}/diaries/{diaryId}/comments")
public class ApiCommentController {
    private final CommentService commentService;
    private final ReplyCreateService replyCreateService;
    private final CommentQueryService commentQueryService;

    @PostMapping
    public ResponseEntity<CommentCreateResponse> createComment(
            @PathVariable Long diaryId,
            @RequestBody @Valid CommentCreateRequest request,
            @RequestAttribute Long memberId
    ) {
        CommentCreateResponse response = commentService.createComment(request, diaryId, memberId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<CommentCreationVerifyResponse> verifyCommentCreation(
            @PathVariable Long diaryId,
            @RequestAttribute Long memberId
    ) {
        CommentCreationVerifyResponse response = commentQueryService.verifyCommentCreation(diaryId, memberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/{commentId}/replies")
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
