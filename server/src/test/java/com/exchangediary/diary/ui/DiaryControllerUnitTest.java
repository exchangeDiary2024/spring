package com.exchangediary.diary.ui;

import com.exchangediary.diary.service.StickerCommandService;
import com.exchangediary.diary.ui.dto.request.StickerRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class DiaryControllerUnitTest {
    @InjectMocks
    private DiaryController diaryController;
    @Mock
    private StickerCommandService stickerCommandService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(diaryController)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void 스티커_붙이기_성공() throws Exception {
        // given
        Long diaryId = 1L;
        Long stickerId = 1L;
        StickerRequest stickerRequest = StickerRequest.builder()
                .coordX(10.0)
                .coordY(10.0)
                .width(200.0)
                .height(150.0)
                .rotation(90.0)
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/diary/"+diaryId+"/sticker/"+stickerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stickerRequest))
        );

        // then
        resultActions.andExpect(status().isCreated());

        verify(stickerCommandService, times(1)).createSticker(stickerRequest, diaryId, stickerId);
    }
}
