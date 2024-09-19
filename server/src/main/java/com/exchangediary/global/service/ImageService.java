package com.exchangediary.global.service;

import com.exchangediary.diary.domain.UploadImageRepository;
import com.exchangediary.diary.domain.entity.PublicationStatus;
import com.exchangediary.diary.domain.entity.UploadImage;
import com.exchangediary.global.domain.StaticImageRepository;
import com.exchangediary.global.domain.entity.StaticImage;
import com.exchangediary.global.domain.entity.StaticImageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final StaticImageRepository staticImageRepository;
    private final UploadImageRepository uploadImageRepository;
    private static final String STATIC_IMAGE_URL = "/api/images/static/";
    private static final String STATIC_IMAGE_NAME = "exchange-static-";
    private static final String UPLOAD_IMAGE_URL = "/api/images/upload/";
    private static final String UPLOAD_IMAGE_NAME = "exchange-upload-";

    /**
     * 사용자가 올린 이미지 업로드
     *
     * @param file   이미지 파일
     * @param status 발행 상태
     * @return
     * @throws IOException
     */
    public UploadImage saveUploadImage(MultipartFile file, PublicationStatus status) throws IOException {
        String extension = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf("."));
        String newFilename = UPLOAD_IMAGE_NAME + UUID.randomUUID().toString() + extension;

        UploadImage image = UploadImage.builder()
                .contentType(file.getContentType())
                .filename(newFilename)
                .image(file.getBytes())
                .status(status)
                .build();

        UploadImage savedImage = uploadImageRepository.save(image);
        savedImage.generateImageUrl(UPLOAD_IMAGE_URL + savedImage.getId());
        return uploadImageRepository.save(savedImage);
    }

    public Optional<UploadImage> getUploadImage(Long id) {
        return uploadImageRepository.findById(id);
    }

    /**
     * static 이미지 업로드
     *
     * @param file 이미지 파일
     * @param type 이미지 종류
     * @return
     * @throws IOException
     */
    public StaticImage saveStaticImage(MultipartFile file, StaticImageType type) throws IOException {
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String newFilename = STATIC_IMAGE_NAME + UUID.randomUUID().toString() + extension;

        StaticImage image = StaticImage.builder()
                .contentType(file.getContentType())
                .filename(newFilename)
                .image(file.getBytes())
                .type(type)
                .build();

        StaticImage savedImage = staticImageRepository.save(image);
        savedImage.generateImageUrl(STATIC_IMAGE_URL + savedImage.getId());
        return staticImageRepository.save(savedImage);
    }

    public Optional<StaticImage> getStaticImage(Long id) {
        return staticImageRepository.findById(id);
    }
}