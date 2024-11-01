package com.exchangediary.diary.domain;

import com.exchangediary.diary.domain.entity.DiaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryContentRepository extends JpaRepository<DiaryContent, Long> {
    List<DiaryContent> findAllByDiaryId(Long diaryId);
}
