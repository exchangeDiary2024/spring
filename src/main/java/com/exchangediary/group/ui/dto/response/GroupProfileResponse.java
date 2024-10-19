package com.exchangediary.group.ui.dto.response;

import com.exchangediary.member.domain.entity.Member;
import lombok.Builder;

import java.util.List;

@Builder
public record GroupProfileResponse(
        List<ImageResponse> selectedImages
) {
    public static GroupProfileResponse from(List<Member> members) {
        List<ImageResponse> imageResponses = members.stream()
                .map(member -> ImageResponse.from(member.getProfileImage()))
                .toList();
        return GroupProfileResponse.builder()
                .selectedImages(imageResponses)
                .build();
    }

    @Builder
    private record ImageResponse(
            String profileImage
    ) {
        public static ImageResponse from(String profileImage) {
            return ImageResponse.builder()
                    .profileImage(profileImage)
                    .build();
        }
    }
}
