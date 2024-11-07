package com.exchangediary.comment.ui;

import com.exchangediary.comment.service.CommentService;
import com.exchangediary.comment.ui.dto.request.CommentRequest;
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
@RequestMapping("/api/groups/{groupId}/diaries/{diaryId}/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(
            @PathVariable Long diaryId,
            @RequestBody @Valid CommentRequest request,
            @RequestAttribute Long memberId
    ) {
        commentService.createComment(request, diaryId, memberId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
