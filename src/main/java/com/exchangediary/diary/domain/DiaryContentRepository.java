package com.exchangediary.diary.domain;

import com.exchangediary.diary.domain.entity.DiaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiaryContentRepository extends JpaRepository<DiaryContent, Long> {
}
