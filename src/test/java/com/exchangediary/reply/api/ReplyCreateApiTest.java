package com.exchangediary.reply.api;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.comment.domain.entity.Comment;
import com.exchangediary.comment.domain.CommentRepository;
import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.domain.enums.GroupRole;
import com.exchangediary.comment.ui.dto.request.ReplyCreateRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class ReplyCreateApiTest extends ApiBaseTest {
    private static final String API_PATH = "/api/groups/%d/diaries/%d/comments/%d/replies";
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private DiaryRepository diaryRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    void 답글_작성_성공 () {
        String content = "답글";

        Group group = createGroup();
        Member diaryCreator = createMember(group);
        Diary diary = createDiary(group, diaryCreator);
        updateSelf(group, 1);
        this.member.updateLastViewableDiaryDate();
        memberRepository.save(member);
        Comment comment = createComment(member, diary);

        RestAssured
                .given().log().all()
                .body(new ReplyCreateRequest(content))
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post(String.format(API_PATH, group.getId(), diary.getId(), comment.getId()))
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 답글_작성_실패_댓글없을경우 () {
        String content = "답글";

        Group group = createGroup();
        Member diaryCreator = createMember(group);
        Diary diary = createDiary(group, diaryCreator);
        updateSelf(group, 1);
        this.member.updateLastViewableDiaryDate();
        memberRepository.save(member);

        RestAssured
                .given().log().all()
                .body(new ReplyCreateRequest(content))
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post(String.format(API_PATH, group.getId(), diary.getId(), 1))
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    @Test
    void 답글_작성_실패_내용없을경우 () {
        String content = "";

        Group group = createGroup();
        Member diaryCreator = createMember(group);
        Diary diary = createDiary(group, diaryCreator);
        updateSelf(group, 1);
        this.member.updateLastViewableDiaryDate();
        memberRepository.save(member);
        Comment comment = createComment(member, diary);

        RestAssured
                .given().log().all()
                .body(new ReplyCreateRequest(content))
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when().post(String.format(API_PATH, group.getId(), diary.getId(), comment.getId()))
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private Group createGroup() {
        return groupRepository.save(Group.from("GROUP_NAME"));
    }

    private void updateSelf(Group group, int order) {
        member.joinGroup("api요청멤버", "orange", order, GroupRole.GROUP_MEMBER, group);
        memberRepository.save(member);
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

    private Diary createDiary(Group group, Member member) {
        Diary diary = Diary.builder()
                .moodLocation("/images/write-page/emoji/sleepy.svg")
                .group(group)
                .member(member)
                .build();
        return diaryRepository.save(diary);
    }

    private Comment createComment(Member member, Diary diary) {
        return commentRepository.save(
                Comment.builder()
                        .xCoordinate(123.45)
                        .yCoordinate(333.33)
                        .content("댓글")
                        .member(member)
                        .diary(diary)
                        .build()
        );
    }
}
