package com.exchangediary.group.ui.dto.response;

import com.exchangediary.member.domain.entity.Member;
import lombok.Builder;

import java.util.List;

@Builder
public record GroupMembersResponse(
        List<GroupMemberResponse> members,
        int selfIndex
) {
    public static GroupMembersResponse of(List<Member> members, int index) {
        List<GroupMemberResponse> response = members.stream()
                .map(GroupMemberResponse::from)
                .toList();
        return GroupMembersResponse.builder()
                .members(response)
                .selfIndex(index)
                .build();
    }

    @Builder
    public record GroupMemberResponse(
            String nickname,
            String profileImage,
            boolean isSelf
    ) {
        public static GroupMemberResponse from(Member member) {
            return GroupMemberResponse.builder()
                    .nickname(member.getNickname())
                    .profileImage(member.getProfileImage())
                    .build();
        }
    }
}
