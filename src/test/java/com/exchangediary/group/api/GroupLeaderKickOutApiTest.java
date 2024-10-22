package com.exchangediary.group.api;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.group.ui.dto.request.GroupKickOutRequest;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.domain.enums.GroupRole;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GroupLeaderKickOutApiTest extends ApiBaseTest {
    private static final String API_PATH = "/api/groups/%d/leader/leave";
    private static final String GROUP_NAME = "버니즈";
    private static final String NICKNAME = "하니";
    @Autowired
    GroupRepository groupRepository;

    @Test
    public void 그룹_강퇴() {
        Group group = createGroup();
        groupRepository.save(group);
        member.updateMemberGroupInfo("api요청멤버", "orange", 1, GroupRole.GROUP_MEMBER, group);
        Member groupMember = createMemberInGroup(group, 2);
        memberRepository.saveAll(Arrays.asList(member, groupMember));

        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(new GroupKickOutRequest(NICKNAME))
                .when()
                .patch(String.format(API_PATH, group.getId()))
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        Member updatedMember = memberRepository.findById(groupMember.getId()).get();
        Group updatedGroup = groupRepository.findById(group.getId()).get();
        assertThat(updatedMember.getGroup()).isEqualTo(null);
        assertThat(updatedMember.getNickname()).isEqualTo(null);
        assertThat(updatedMember.getGroupRole()).isEqualTo(null);
        assertThat(updatedMember.getProfileImage()).isEqualTo(null);
        assertThat(updatedMember.getOrderInGroup()).isEqualTo(0);
        assertThat(updatedGroup.getCurrentOrder()).isEqualTo(1);
    }

    private Group createGroup() {
        return Group.builder()
                .name(GROUP_NAME)
                .currentOrder(0)
                .code("code")
                .build();
    }

    private Member createMemberInGroup(Group group, int orderInGroup) {
        return Member.builder()
                .kakaoId(12345L)
                .nickname(NICKNAME)
                .orderInGroup(orderInGroup)
                .profileImage("red")
                .group(group)
                .build();
    }
}
