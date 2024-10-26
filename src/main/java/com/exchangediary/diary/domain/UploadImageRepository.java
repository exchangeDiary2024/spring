package com.exchangediary.diary.domain;

import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.entity.UploadImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UploadImageRepository extends JpaRepository<UploadImage, Long> {
    Optional<UploadImage> findByDiary(Diary diary);
}
