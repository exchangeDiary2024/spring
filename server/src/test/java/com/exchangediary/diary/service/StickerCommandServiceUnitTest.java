package com.exchangediary.diary.service;

import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.diary.domain.StickerRepository;
import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.entity.Sticker;
import com.exchangediary.diary.ui.dto.request.StickerRequest;
import com.exchangediary.global.domain.StaticImageRepository;
import com.exchangediary.global.domain.entity.StaticImage;
import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.GlobalException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class StickerCommandServiceUnitTest {
    @InjectMocks
    StickerCommandService stickerCommandService;
    @Mock
    StickerRepository stickerRepository;
    @Mock
    DiaryRepository diaryRepository;
    @Mock
    StaticImageRepository staticImageRepository;

    @Test
    void 스티커_붙이기_성공() {
        // given
        long diaryId = 1L;
        long stickerId = 1L;
        StickerRequest stickerRequest = mock(StickerRequest.class);
        Diary mockDiary = mock(Diary.class);
        StaticImage mockStaticImage = mock(StaticImage.class);
        Sticker mockSticker = mock(Sticker.class);

        when(diaryRepository.findById(diaryId)).thenReturn(Optional.ofNullable(mockDiary));
        when(staticImageRepository.findById(stickerId)).thenReturn(Optional.ofNullable(mockStaticImage));
        when(stickerRepository.countByDiaryId(diaryId)).thenReturn(0);
        when(stickerRepository.save(any(Sticker.class))).thenReturn(mockSticker);

        // when
        stickerCommandService.createSticker(stickerRequest, diaryId, stickerId);

        // then
        verify(diaryRepository, times(1)).findById(diaryId);
        verify(staticImageRepository, times(1)).findById(stickerId);
        verify(stickerRepository, times(1)).countByDiaryId(diaryId);
        verify(stickerRepository, times(1)).save(any(Sticker.class));
    }

    @Test
    void 스티커_붙이기_실패_일기없음() {
        // given
        long diaryId = 2L;
        long stickerId = 1L;
        StickerRequest stickerRequest = mock(StickerRequest.class);

        when(diaryRepository.findById(diaryId)).thenThrow(new GlobalException(ErrorCode.DIARY_NOT_FOUND));

        // when
        GlobalException exception = assertThrows(GlobalException.class,
                () -> stickerCommandService.createSticker(stickerRequest, diaryId, stickerId));

        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DIARY_NOT_FOUND);

        verify(diaryRepository, times(1)).findById(diaryId);
        verify(staticImageRepository, times(0)).findById(stickerId);
        verify(stickerRepository, times(0)).countByDiaryId(diaryId);
        verify(stickerRepository, times(0)).save(any(Sticker.class));
    }

    @Test
    void 스티커_붙이기_실패_스티커이미지없음() {
        // given
        long diaryId = 1L;
        long stickerId = 2L;
        StickerRequest stickerRequest = mock(StickerRequest.class);

        when(diaryRepository.findById(diaryId)).thenReturn(Optional.ofNullable(mock(Diary.class)));
        when(staticImageRepository.findById(stickerId)).thenThrow(new GlobalException(ErrorCode.STICKER_IMAGE_NOT_FOUND));

        // when
        GlobalException exception = assertThrows(GlobalException.class,
                () -> stickerCommandService.createSticker(stickerRequest, diaryId, stickerId));

        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.STICKER_IMAGE_NOT_FOUND);

        verify(diaryRepository, times(1)).findById(diaryId);
        verify(staticImageRepository, times(1)).findById(stickerId);
        verify(stickerRepository, times(0)).countByDiaryId(diaryId);
        verify(stickerRepository, times(0)).save(any(Sticker.class));
    }
}
