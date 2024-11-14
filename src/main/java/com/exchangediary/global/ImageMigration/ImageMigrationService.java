package com.exchangediary.global.ImageMigration;

import com.exchangediary.diary.domain.UploadImageRepository;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.entity.UploadImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageMigrationService {
    private final UploadImageRepository uploadImageRepository;

    @Transactional
    public void migrateImagesToFileSystem() {
        List<UploadImage> uploadImages = uploadImageRepository.findAll();

        for (UploadImage uploadImage : uploadImages) {
            byte[] imageBytes = uploadImage.getImage();

            Diary diary = uploadImage.getDiary();
            Long groupId = diary.getGroup().getId();
            String imagePath = System.getProperty("user.dir") + "/src/main/resources/static/images/upload/groups/" + groupId;
            saveImageToFileSystem(imageBytes, imagePath, uploadImage);
        }
    }

    private void saveImageToFileSystem(byte[] imageBytes, String imagePath, UploadImage uploadImage) {
        File directory = new File(imagePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String date = uploadImage.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String fileExtension = ".jpg";

        File newImageFile = new File(imagePath + "/" + date + fileExtension);
        try (FileOutputStream fos = new FileOutputStream(newImageFile)) {
            fos.write(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException("이미지 마이그레이션 실패: " + uploadImage.getDiary().getId(), e);
        }
    }
}
