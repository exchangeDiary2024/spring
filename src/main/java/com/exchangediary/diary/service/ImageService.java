package com.exchangediary.diary.service;

import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.FailedImageUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
public class ImageService {
    private static final Set<String> VALID_IMAGE_FORMAT = Set.of(
            "image/jpeg",
            "image/gif",
            "image/png",
            "image/heic",
            "image/heif"
    );

    private void saveImage(MultipartFile file, Diary diary, String groupId) throws IOException{
        if (!isEmptyFile(file)) {
            validateImageType(file);
            String imagePath = System.getProperty("user.dir") + "/src/main/resources/static/images/upload/groups/" + groupId;
            File directory = new File(imagePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String fileExtension = getFileExtension(file.getOriginalFilename());
            file.transferTo(new File(imagePath + "/" + date + fileExtension));
            diary.uploadImageFileName(date + fileExtension);
        }
    }

    private boolean isEmptyFile(MultipartFile file) {
        return file == null || file.isEmpty();
    }

    private void validateImageType(MultipartFile file) {
        String contentType = file.getContentType();

        if (!VALID_IMAGE_FORMAT.contains(contentType)) {
            throw new FailedImageUploadException(
                    ErrorCode.INVALID_IMAGE_FORMAT,
                    "",
                    file.getOriginalFilename()
            );
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return fileName.substring(dotIndex);
    }
}
