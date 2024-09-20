package com.exchangediary.diary.service;

import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.entity.PublicationStatus;
import com.exchangediary.diary.domain.entity.UploadImage;
import com.exchangediary.diary.ui.dto.request.DiaryRequest;
import com.exchangediary.diary.ui.dto.request.UploadImageRequest;
import com.exchangediary.global.domain.StaticImageRepository;
import com.exchangediary.global.domain.entity.StaticImage;
import com.exchangediary.global.service.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiaryCommandServiceTest {
    @InjectMocks
    private DiaryCommandService diaryCommandService;
    @Mock
    private DiaryRepository diaryRepository;
    @Mock
    private StaticImageRepository staticImageRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private MultipartFile file;

    @Test
    void createDiarySuccess() throws Exception {
        //given
        DiaryRequest diaryRequest = diaryRequest();
        UploadImageRequest uploadImageRequest = uploadImageRequest();
        StaticImage moodImage = mock(StaticImage.class);
        UploadImage uploadImage = mock(UploadImage.class);
        Diary expectedDiary = Diary.of(diaryRequest, moodImage, uploadImage);

        when(staticImageRepository.findById(diaryRequest.todayMoodId()))
                .thenReturn(Optional.of(moodImage));
        when(imageService.saveUploadImage(file, PublicationStatus.PUBLISHED))
                .thenReturn(uploadImage);
        when(diaryRepository.save(any(Diary.class))).thenReturn(expectedDiary);

        //when
        Diary actualDiary = diaryCommandService.createDiary(diaryRequest, uploadImageRequest);

        //then
        assertThat(actualDiary).isNotNull();
        assertThat(expectedDiary.getId()).isEqualTo(actualDiary.getId());
        verify(diaryRepository).save(any(Diary.class));
        verify(staticImageRepository).findById(diaryRequest.todayMoodId());
        verify(imageService).saveUploadImage(file, PublicationStatus.PUBLISHED);
    }

    private DiaryRequest diaryRequest() {
        return DiaryRequest.builder()
                .content("내용")
                .todayMoodId(1L)
                .build();
    }

    private UploadImageRequest uploadImageRequest() {
        return UploadImageRequest.builder()
                .file(file)
                .build();
    }

}