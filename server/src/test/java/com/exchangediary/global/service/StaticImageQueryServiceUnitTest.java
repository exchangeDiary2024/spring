package com.exchangediary.global.service;

import com.exchangediary.global.domain.StaticImageRepository;
import com.exchangediary.global.domain.entity.StaticImage;
import com.exchangediary.global.domain.entity.StaticImageType;
import com.exchangediary.global.ui.dto.response.MoodsResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class StaticImageQueryServiceUnitTest {
    @InjectMocks
    private StaticImageQueryService staticImageQueryService;
    @Mock
    private StaticImageRepository staticImageRepository;

    @Test
    void 기분목록조회성공() {
        //given
        doReturn(staticImages())
                .when(staticImageRepository)
                .findAllByType(StaticImageType.MOOD);

        //when
        MoodsResponse moods = staticImageQueryService.findMoods();

        //then
        assertThat(moods.moods().size()).isEqualTo(3);
        assertThat(moods.moods().get(0).id()).isEqualTo(1);
        assertThat(moods.moods().get(0).imageUrl()).isEqualTo("moodImageUrl1");
        assertThat(moods.moods().get(1).id()).isEqualTo(2);
        assertThat(moods.moods().get(1).imageUrl()).isEqualTo("moodImageUrl2");
        assertThat(moods.moods().get(2).id()).isEqualTo(3);
        assertThat(moods.moods().get(2).imageUrl()).isEqualTo("moodImageUrl3");
    }

    private List<StaticImage> staticImages() {
        List<String> dummy = List.of("moodImageUrl1", "moodImageUrl2", "moodImageUrl3");
        AtomicLong count = new AtomicLong(1);
        return dummy.stream().map(url -> StaticImage.builder()
                        .id(count.getAndIncrement())
                        .url(url)
                        .type(StaticImageType.MOOD)
                        .build())
                .toList();
    }
}