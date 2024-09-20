package com.exchangediary.diary.ui;

import com.exchangediary.diary.service.StickerCommandService;
import com.exchangediary.diary.ui.dto.request.StickerRequest;
import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.GlobalException;
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

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
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

    @Test
    void 스티커_붙이기_실패_일기없음() throws Exception {
        // given
        Long diaryId = 2L;
        Long stickerId = 1L;
        StickerRequest stickerRequest = StickerRequest.builder()
                .coordX(10.0)
                .coordY(10.0)
                .width(200.0)
                .height(150.0)
                .rotation(90.0)
                .build();

        doThrow(new GlobalException(ErrorCode.DIARY_NOT_FOUND))
                .when(stickerCommandService)
                .createSticker(stickerRequest, diaryId, stickerId);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/diary/"+diaryId+"/sticker/"+stickerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stickerRequest))
        );

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void 스티커_붙이기_실패_스티커이미지없음() throws Exception {
        // given
        Long diaryId = 1L;
        Long stickerId = 2L;
        StickerRequest stickerRequest = StickerRequest.builder()
                .coordX(10.0)
                .coordY(10.0)
                .width(200.0)
                .height(150.0)
                .rotation(90.0)
                .build();

        doThrow(new GlobalException(ErrorCode.STICKER_IMAGE_NOT_FOUND))
                .when(stickerCommandService)
                .createSticker(stickerRequest, diaryId, stickerId);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/diary/"+diaryId+"/sticker/"+stickerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stickerRequest))
        )
                .andExpect(
                        // assert로 예외를 검사하는 람다 사용
                        (rslt) -> assertTrue(rslt.getResolvedException().getClass().isAssignableFrom(GlobalException.class)));
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof GlobalException));

//                .andExpect((result) -> {assertThat(Objects.requireNonNull(result.getResolvedException()).getClass()).isEqualTo(GlobalException.class);});

        // then
//        resultActions.andExpect(status().isNotFound());
//                ;
    }
}
