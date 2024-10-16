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
                .map(member -> ImageResponse.from(member.getProfileLocation()))
                .toList();
        return GroupProfileResponse.builder()
                .selectedImages(imageResponses)
                .build();
    }

    @Builder
    private record ImageResponse(
            String profileLocation
    ) {
        public static ImageResponse from(String profileLocation) {
            return ImageResponse.builder()
                    .profileLocation(profileLocation)
                    .build();
        }
    }
}
