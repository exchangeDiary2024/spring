package com.exchangediary.comment.api;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.comment.domain.CommentRepository;
import com.exchangediary.comment.domain.ReplyRepository;
import com.exchangediary.comment.domain.entity.Comment;
import com.exchangediary.comment.domain.entity.Reply;
import com.exchangediary.comment.ui.dto.response.CommentResponse;
import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.domain.enums.GroupRole;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentViewApiTest extends ApiBaseTest {
    private static final String API_PATH = "/api/groups/%s/diaries/%d/comments/%d";
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private DiaryRepository diaryRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReplyRepository replyRepository;

    @Test
    void 댓글_조회_성공() {
        Group group = createGroup();
        updateSelf(group, 1);
        this.member.updateLastViewableDiaryDate();
        memberRepository.save(member);
        Member diaryCreator = createMember(group);
        Diary diary = createDiary(group, diaryCreator);
        Comment comment = createComment(member, diary);
        createReply(member, comment);

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get(String.format(API_PATH, group.getId(), diary.getId(), comment.getId()))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(CommentResponse.class);
        var replies = response.replies();

        assertThat(replies.getFirst().content()).isEqualTo("답글");
        assertThat(replies.getFirst().profileImage()).isEqualTo("orange");
        assertThat(response.content()).isEqualTo("댓글");
        assertThat(response.profileImage()).isEqualTo("orange");
    }

    @Test
    void 댓글_조회_성공_답글_없을_경우() {
        Group group = createGroup();
        updateSelf(group, 1);
        this.member.updateLastViewableDiaryDate();
        memberRepository.save(member);
        Member diaryCreator = createMember(group);
        Diary diary = createDiary(group, diaryCreator);
        Comment comment = createComment(member, diary);

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get(String.format(API_PATH, group.getId(), diary.getId(), comment.getId()))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(CommentResponse.class);
        var replies = response.replies();

        assertThat(replies).isEmpty();
        assertThat(response.content()).isEqualTo("댓글");
        assertThat(response.profileImage()).isEqualTo("orange");
    }

    @Test
    void 댓글_조회_실패_댓글_없을_경우() {
        Group group = createGroup();
        updateSelf(group, 1);
        this.member.updateLastViewableDiaryDate();
        memberRepository.save(member);
        Member diaryCreator = createMember(group);
        Diary diary = createDiary(group, diaryCreator);

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get(String.format(API_PATH, group.getId(), diary.getId(), 10))
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
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

    private Comment createComment(Member member, Diary diary) {
        return commentRepository.save(
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

    private void createReply(Member member, Comment comment) {
        replyRepository.save(
                Reply.builder()
                        .content("답글")
                        .member(member)
                        .comment(comment)
                        .build()
        );
    }
}
