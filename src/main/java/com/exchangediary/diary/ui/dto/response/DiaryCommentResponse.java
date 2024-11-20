package com.exchangediary.diary.ui.dto.response;

import com.exchangediary.comment.domain.entity.Comment;
import lombok.Builder;

import java.util.List;

@Builder
public record DiaryCommentResponse(
        Long id,
        String profileImage,
        Double xCoordinate,
        Double yCoordinate
) {
    public static List<DiaryCommentResponse> fromComments(List<Comment> comments) {
        return comments.stream()
                .map(DiaryCommentResponse::from)
                .toList();
    }

    private static DiaryCommentResponse from(Comment comment) {
        return DiaryCommentResponse.builder()
                .id(comment.getId())
                .profileImage(comment.getMember().getProfileImage())
                .xCoordinate(comment.getXCoordinate())
                .yCoordinate(comment.getYCoordinate())
                .build();
    }
}
