package com.exchangediary.global.ImageMigration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageMigrationInitializer {
    private final ImageMigrationService imageMigrationService;

    @PostConstruct
    public void migrateImages() {
        System.out.println("PostConstructTestService @PostConstruct 호출");
        imageMigrationService.migrateImagesToFileSystem();
    }
}
