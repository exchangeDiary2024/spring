package com.exchangediary.global.config.interceptor;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.enums.GroupRole;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class BelongToGroupInterceptorTest extends ApiBaseTest {
    @Autowired
    private GroupRepository groupRepository;

    @Test
    void 그룹_가입하지_않은_사용자가_그룹생성가입페이지_접근() {
        RestAssured
                .given().log().all()
                .cookie("token", token)
                .redirects().follow(false)
                .when().get("/group")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 그룹_가입한_사용자가_그룹월별형페이지_접근() {
        Group group = createGroup();
        updateSelf(group, 1);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .redirects().follow(false)
                .when().get("/group/" + group.getId())
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 그룹_가입한_사용자가_그룹생성가입페이지_접근() {
        Group group = createGroup();
        updateSelf(group, 1);

        String location = RestAssured
                .given().log().all()
                .cookie("token", token)
                .redirects().follow(false)
                .when().get("/group")
                .then()
                .log().status()
                .log().headers()
                .statusCode(HttpStatus.FOUND.value())
                .extract()
                .header("location");

        assertThat(location.substring(location.indexOf("/group"))).isEqualTo("/group/" + group.getId());
    }

    @Test
    void 그룹_가입하지_않은_사용자가_그룹월별형페이지_접근() {
        Group group = createGroup();

        String location = RestAssured
                .given().log().all()
                .cookie("token", token)
                .redirects().follow(false)
                .when().get("/group/" + group.getId())
                .then()
                .log().status()
                .log().headers()
                .statusCode(HttpStatus.FOUND.value())
                .extract()
                .header("location");

        assertThat(location.substring(location.indexOf("/group"))).isEqualTo("/group");
    }

    private Group createGroup() {
        return groupRepository.save(Group.of("group-name", "code"));
    }

    private void updateSelf(Group group, int order) {
        this.member.joinGroup(
                "me",
                "red",
                order,
                GroupRole.GROUP_MEMBER,
                group
        );
        memberRepository.save(this.member);
    }
}
