package com.exchangediary.diary.domain;

import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.entity.Sticker;
import com.exchangediary.global.domain.entity.StaticImage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StickerRepositoryUnitTest {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private StickerRepository stickerRepository;

    @Test
    void 스티커_저장() {
        StaticImage staticImage = StaticImage.builder().build();
        Diary diary = Diary.builder().build();
        entityManager.persist(staticImage);
        entityManager.persist(diary);
        Sticker sticker = Sticker.builder()
                .coordX(10.0)
                .coordY(20.0)
                .coordZ(1)
                .height(200.0)
                .width(100.0)
                .rotation(90.0)
                .staticImage(staticImage)
                .diary(diary)
                .build();

        Sticker saved = stickerRepository.save(sticker);

        assertThat(saved.getCoordX()).isEqualTo(sticker.getCoordX());
        assertThat(saved.getCoordY()).isEqualTo(sticker.getCoordY());
        assertThat(saved.getCoordZ()).isEqualTo(sticker.getCoordZ());
        assertThat(saved.getHeight()).isEqualTo(sticker.getHeight());
        assertThat(saved.getWidth()).isEqualTo(sticker.getWidth());
        assertThat(saved.getRotation()).isEqualTo(sticker.getRotation());
        assertThat(saved.getStaticImage().getId()).isEqualTo(sticker.getStaticImage().getId());
        assertThat(saved.getDiary().getId()).isEqualTo(sticker.getDiary().getId());
    }

    @Test
    void 일기에_붙여진_스티커_개수() {
        Diary diary = Diary.builder().build();
        Sticker sticker = Sticker.builder()
                .diary(diary)
                .build();
        entityManager.persist(diary);
        entityManager.persist(sticker);

        int countOfSticker = stickerRepository.countByDiaryId(diary.getId());

        assertThat(countOfSticker).isEqualTo(1);
    }
}
