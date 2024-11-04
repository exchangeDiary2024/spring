package com.exchangediary.notification.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class FCMInitializer {
    private static final String FIREBASE_CONFIG_PATH = "buddies-spring-firebase-adminsdk-fadag-ba724eb696.json";

    @PostConstruct
    public void initialize() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream());
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();
            FirebaseApp.initializeApp(options);
            log.info("FCM initialize");
        } catch (IOException exception) {
            log.error("{}", exception.getMessage());
        }
    }
}
