package com.exchangediary.global.domain;

import com.exchangediary.global.domain.entity.StaticImage;
import com.exchangediary.global.domain.entity.StaticImageType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class StaticImageRepositoryTest {
    @Autowired
    private StaticImageRepository staticImageRepository;

    @Test
    public void 레포지토리가null이아님() {
        Assertions.assertThat(staticImageRepository).isNotNull();
    }

    @Test
    public void 기분이미지등록() {
        //given
        final StaticImage image = StaticImage.builder()
                .type(StaticImageType.MOOD)
                .url("moodImageUrl")
                .build();
        //when
        final StaticImage newImage = staticImageRepository.save(image);

        //then
        Assertions.assertThat(newImage.getId()).isNotNull();
        Assertions.assertThat(newImage.getType()).isEqualTo(StaticImageType.MOOD);
        Assertions.assertThat(newImage.getUrl()).isEqualTo("moodImageUrl");
    }
}