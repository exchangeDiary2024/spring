package com.exchangediary.diary.domain;

import com.exchangediary.diary.domain.entity.UploadImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadImageRepository extends JpaRepository<UploadImage, Long> {
}
