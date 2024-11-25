package com.exchangediary.comment.api;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.comment.domain.CommentRepository;
import com.exchangediary.comment.domain.entity.Comment;
import com.exchangediary.comment.ui.dto.response.CommentCreationVerifyResponse;
import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.domain.enums.GroupRole;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CommentCreationVerifyApiTest extends ApiBaseTest {
    private static final String API_PATH = "/api/groups/%s/diaries/%d/comments/verify";
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private DiaryRepository diaryRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    void 댓글창_생성_성공() {
        Group group = createGroup();
        Member diaryCreator = createMember(group);
        Diary diary = createDiary(group, diaryCreator);
        updateSelf(group, 1);
        this.member.updateLastViewableDiaryDate();
        memberRepository.save(member);

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().get(String.format(API_PATH, group.getId(), diary.getId()))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(CommentCreationVerifyResponse.class);

        assertThat(response.profileImage()).isEqualTo(member.getProfileImage());
    }

    @Test
    void 댓글창_생성_실패_이미_생성() {
        Group group = createGroup();
        Member diaryCreator = createMember(group);
        Diary diary = createDiary(group, diaryCreator);
        updateSelf(group, 1);
        this.member.updateLastViewableDiaryDate();
        memberRepository.save(member);
        createComment(member, diary);

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().get(String.format(API_PATH, group.getId(), diary.getId()))
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract().response();

        String message = response.jsonPath().getString("message");
        assertThat(message).isEqualTo("댓글은 한 번 남길 수 있어요!");
    }

    @Test
    void 댓글창_생성_실패_일기_작성자() {
        Group group = createGroup();
        Diary diary = createDiary(group, member);
        updateSelf(group, 1);
        this.member.updateLastViewableDiaryDate();
        memberRepository.save(member);

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().get(String.format(API_PATH, group.getId(), diary.getId()))
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract().response();

        String message = response.jsonPath().getString("message");
        assertThat(message).isEqualTo("내 일기에는 댓글을 남길 수 없어요!");
    }

    private Group createGroup() {
        return groupRepository.save(Group.from("GROUP_NAME"));
    }

    private void updateSelf(Group group, int order) {
        member.joinGroup("api요청멤버", "orange", order, GroupRole.GROUP_MEMBER, group);
        memberRepository.save(member);
    }

    private Diary createDiary(Group group, Member member) {
        Diary diary = Diary.builder()
                .todayMood("sleepy.svg")
                .group(group)
                .member(member)
                .build();
        return diaryRepository.save(diary);
    }

    private Member createMember(Group group) {
        Member member = Member.builder()
                .profileImage("orange")
                .kakaoId(1234L)
                .orderInGroup(2)
                .nickname("하니")
                .groupRole(GroupRole.GROUP_MEMBER)
                .group(group)
                .build();
        memberRepository.save(member);
        return member;
    }

    private void createComment(Member member, Diary diary) {
        commentRepository.save(
                Comment.builder()
                        .xCoordinate(123.45)
                        .yCoordinate(333.33)
                        .page(1)
                        .content("댓글")
                        .member(member)
                        .diary(diary)
                        .build()
        );
    }
}
