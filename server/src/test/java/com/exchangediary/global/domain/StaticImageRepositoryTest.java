package com.exchangediary.global.domain;

import com.exchangediary.global.domain.entity.StaticImage;
import com.exchangediary.global.domain.entity.StaticImageType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StaticImageRepositoryTest {
    @Autowired
    private StaticImageRepository staticImageRepository;

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
        assertThat(newImage.getId()).isNotNull();
        assertThat(newImage.getType()).isEqualTo(StaticImageType.MOOD);
        assertThat(newImage.getUrl()).isEqualTo("moodImageUrl");
    }

    @Test
    public void 기분이미지조회() {
        //given
        final StaticImage image = StaticImage.builder()
                .type(StaticImageType.MOOD)
                .url("moodImageUrl")
                .build();

        //when
        staticImageRepository.save(image);
        List<StaticImage> findResult = staticImageRepository.findAllByType(StaticImageType.MOOD);

        //then
        assertThat(findResult).isNotNull();
        assertThat(findResult.get(0).getId()).isNotNull();
        assertThat(findResult.get(0).getType()).isEqualTo(StaticImageType.MOOD);
        assertThat(findResult.get(0).getUrl()).isEqualTo("moodImageUrl");
    }
}