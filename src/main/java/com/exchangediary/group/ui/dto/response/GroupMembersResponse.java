package com.exchangediary.group.ui.dto.response;

import com.exchangediary.member.domain.entity.Member;
import lombok.Builder;

import java.util.List;

@Builder
public record GroupMembersResponse(
        List<MemberResponse> members
) {
    public static GroupMembersResponse from(List<Member> members) {
        List<MemberResponse> memberResponses = members.stream()
                .map(MemberResponse::from)
                .toList();
        return GroupMembersResponse.builder()
                .members(memberResponses)
                .build();
    }

    @Builder
    private record MemberResponse(
            String nickname,
            String profileLocation
    ) {
        public static MemberResponse from(Member member) {
            return MemberResponse.builder()
                    .nickname(member.getNickname())
                    .profileLocation(member.getProfileLocation())
                    .build();
        }
    }
}
