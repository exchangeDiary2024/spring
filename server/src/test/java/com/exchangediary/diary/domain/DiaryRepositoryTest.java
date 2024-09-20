package com.exchangediary.diary.domain;

import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.entity.UploadImage;
import com.exchangediary.global.domain.entity.StaticImage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DataJpaTest
public class DiaryRepositoryTest {
    @Autowired
    private DiaryRepository diaryRepository;

    @Test
    public void 일기등록() {
        //given
        StaticImage moodImage = mock(StaticImage.class);
        UploadImage uploadImage = mock(UploadImage.class);
        Diary newDiary = diary(moodImage, uploadImage);

        //when
        Diary savedDiary = diaryRepository.save(newDiary);

        //then
        assertThat(savedDiary).isNotNull();
        assertThat(savedDiary.getId()).isEqualTo(newDiary.getId());
        assertThat(savedDiary.getContent()).isEqualTo("내용");
    }

    private Diary diary(StaticImage moodImage, UploadImage uploadImage) {
        return Diary.builder()
                .content("내용")
                .moodImage(moodImage)
                .uploadImage(uploadImage)
                .build();
    }
}