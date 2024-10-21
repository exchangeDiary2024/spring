package com.exchangediary.group.api;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.MemberRepository;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.domain.enums.GroupRole;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GroupLeaveApiTest extends ApiBaseTest {
    private static final String API_PATH = "/api/groups/%d/leave";
    private static final String GROUP_NAME = "버니즈";

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 그룹_나가기() {
        Group group = createGroup();
        groupRepository.save(group);
        member.updateMemberGroupInfo("api요청멤버", "orange", 1, GroupRole.GROUP_MEMBER, group);
        memberRepository.save(member);

        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when()
                .patch(String.format(API_PATH, group.getId()))
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        Member updatedMember = memberRepository.findById(member.getId()).get();
        assertThat(updatedMember.getGroup()).isEqualTo(null);
    }

    private Group createGroup() {
        return Group.builder()
                .name(GROUP_NAME)
                .currentOrder(0)
                .code("code")
                .build();
    }
}
