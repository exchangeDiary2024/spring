package com.exchangediary.global.ui;

import com.exchangediary.global.service.StaticImageQueryService;
import com.exchangediary.global.ui.dto.response.MoodsResponse;
import com.exchangediary.global.ui.dto.response.StaticImageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StaticImageControllerUnitTest {
    @InjectMocks
    private StaticImageController staticImageController;
    @Mock
    private StaticImageQueryService staticImageService;
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(staticImageController)
                .build();
    }

    @Test
    void 기분목록조회성공() throws Exception {
        //given
        MoodsResponse response = moodsResponse();

        doReturn(response)
                .when(staticImageService)
                .findMoods();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/moods")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("moods", response.moods()).exists())
                .andExpect(jsonPath("$.moods[0].id").value(1L))
                .andExpect(jsonPath("$.moods[0].imageUrl").value("moodImageUrl1"))
                .andExpect(jsonPath("$.moods[1].id").value(2L))
                .andExpect(jsonPath("$.moods[1].imageUrl").value("moodImageUrl2"))
                .andExpect(jsonPath("$.moods[2].id").value(3L))
                .andExpect(jsonPath("$.moods[2].imageUrl").value("moodImageUrl3"))
                .andReturn();
    }

    private MoodsResponse moodsResponse() {
        return MoodsResponse.builder()
                .moods(staticImagesResponse())
                .build();
    }

    private List<StaticImageResponse> staticImagesResponse() {
        List<String> dummy = List.of("moodImageUrl1", "moodImageUrl2", "moodImageUrl3");
        AtomicLong counter = new AtomicLong(1);

        return dummy.stream()
                .map(staticImageUrl -> StaticImageResponse.builder()
                        .id(counter.getAndIncrement())
                        .imageUrl(staticImageUrl)
                        .build())
                .toList();
    }
}