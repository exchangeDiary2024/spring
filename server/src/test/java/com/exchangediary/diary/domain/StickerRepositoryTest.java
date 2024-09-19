package com.exchangediary.diary.domain;

import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.domain.entity.Sticker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StickerRepositoryTest {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private StickerRepository stickerRepository;

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
