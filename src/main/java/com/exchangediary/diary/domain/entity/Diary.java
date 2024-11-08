package com.exchangediary.diary.domain.entity;

import com.exchangediary.comment.domain.entity.Comment;
import com.exchangediary.diary.ui.dto.request.DiaryRequest;
import com.exchangediary.global.domain.entity.BaseEntity;
import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.LongVarcharJdbcType;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED, force = true)
@AllArgsConstructor(access = PRIVATE)
public class Diary extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;
    @Lob
    @JdbcType(LongVarcharJdbcType.class)
    @NotNull
    private final String moodLocation;
    private String imageFileName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private final Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    @NotNull
    private final Group group;
    @OneToMany(mappedBy = "diary")
    @OrderBy("page ASC")
    private List<DiaryContent> contents;
    @OneToMany(mappedBy = "diary")
    private List<Comment> comments;

    public static Diary of(DiaryRequest diaryRequest, Member member, Group group) {
        return Diary.builder()
                .moodLocation(diaryRequest.moodLocation())
                .member(member)
                .group(group)
                .build();
    }

    public void uploadImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}
