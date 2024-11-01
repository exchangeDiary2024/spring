package com.exchangediary.diary.api;

import com.exchangediary.ApiBaseTest;
import com.exchangediary.diary.domain.DiaryContentRepository;
import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.diary.domain.dto.DiaryContentDto;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.entity.DiaryContent;
import com.exchangediary.diary.ui.dto.response.DiaryResponse;
import com.exchangediary.group.domain.GroupRepository;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.domain.enums.GroupRole;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class DiaryViewApiTest extends ApiBaseTest {
    private static final String API_PATH = "/api/groups/%d/diaries";
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private DiaryRepository diaryRepository;
    @Autowired
    private DiaryContentRepository diaryContentRepository;

    @Test
    void 일기_조회_성공() {
        Group group = createGroup();
        this.member.joinGroup("self", "red", 1, GroupRole.GROUP_MEMBER, group);
        this.member.updateLastViewableDiaryDate();
        memberRepository.save(member);
        Member diaryCreator = createMember(group);
        Diary diary = createDiary(diaryCreator, group);
        createDiaryContent(diary);

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .when()
                .get(String.format(API_PATH, group.getId()) + "/" + diary.getId())
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(DiaryResponse.class);
        var diaryContents = response.contents();

        assertThat(diaryContents).hasSize(3);
        assertThat(diaryContents.get(0).content()).isEqualTo("hi");
        assertThat(diaryContents.get(1).content()).isEqualTo("hi");
        assertThat(diaryContents.get(2).content()).isEqualTo("hi");
        assertThat(response.moodLocation()).isEqualTo("/images/write-page/emoji/sleepy.svg");
        assertThat(response.imageFileName()).isEqualTo("20241101.jpeg");
        assertThat(response.nickname()).isEqualTo("하니");
        assertThat(response.profileImage()).isEqualTo("orange");
    }

    @Test
    void 일기_조회_실패_일기_없음 () {
        Group group = createGroup();
        this.member.joinGroup("self", "red", 1, GroupRole.GROUP_MEMBER, group);
        this.member.updateLastViewableDiaryDate();
        memberRepository.save(member);
        Long diaryId = 1L;

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .redirects().follow(false)
                .when()
                .get(String.format(API_PATH, group.getId()) + "/" + diaryId)
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private Group createGroup() {
        return groupRepository.save(Group.of("GROUP_NAME", "code"));
    }

    private Diary createDiary(Member member, Group group) {
        Diary diary = Diary.builder()
                .moodLocation("/images/write-page/emoji/sleepy.svg")
                .imageFileName("20241101.jpeg")
                .group(group)
                .member(member)
                .build();
        diaryRepository.save(diary);
        return diary;
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

    private void createDiaryContent(Diary diary) {
        DiaryContentDto diaryContent = DiaryContentDto.from("hi");
        List<DiaryContent> diaryContents = IntStream.rangeClosed(1, 3)
                .mapToObj(page -> DiaryContent.from(page, diaryContent, diary))
                .toList();
        diaryContentRepository.saveAll(diaryContents);
    }
}
