package com.exchangediary.diary.domain.entity;

import com.exchangediary.diary.domain.dto.DiaryContentDto;
import com.exchangediary.global.domain.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED, force = true)
@AllArgsConstructor(access = PRIVATE)
public class DiaryContent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_content_id")
    private Long id;
    @NotNull
    private final Integer page;
    @NotNull
    private final String content;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dairy_id", foreignKey = @ForeignKey(name = "content_diary_id_fkey"))
    private final Diary diary;

    public static DiaryContent from(int page, DiaryContentDto diaryContentDto, Diary diary) {
        return DiaryContent.builder()
                .page(page)
                .content(diaryContentDto.content())
                .diary(diary)
                .build();
    }
}
