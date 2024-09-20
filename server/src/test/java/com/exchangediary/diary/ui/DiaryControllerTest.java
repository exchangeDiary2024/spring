package com.exchangediary.diary.ui;

import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.service.DiaryCommandService;
import com.exchangediary.diary.ui.dto.request.DiaryRequest;
import com.exchangediary.diary.ui.dto.request.UploadImageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DiaryControllerTest {
    @InjectMocks
    private DiaryController diaryController;
    @Mock
    private DiaryCommandService diaryCommandService;
    private MockMvc mockMvc;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(diaryController)
                .build();
    }

    @Test
    void 일기작성성공() throws Exception {
        //given
        Long expectedDiaryId = 1L;
        Diary diaryMock = mock(Diary.class);

        when(diaryMock.getId()).thenReturn(expectedDiaryId);
        when(diaryCommandService
                .createDiary(any(DiaryRequest.class), any(UploadImageRequest.class)))
                .thenReturn(diaryMock);
        MockMultipartFile dataPart = new MockMultipartFile(
                "data",
                "data",
                MediaType.APPLICATION_JSON_VALUE,
                "{\"content\":\"내용\",\"todayMoodId\":1}".getBytes()
        );

        // When
        ResultActions resultActions = mockMvc.perform(
                multipart("/diary")
                        .file("file", new byte[0])
                        .file(dataPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        //then
        MvcResult mvcResult = resultActions.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/diary/1/"))
                .andExpect(content().string(diaryMock.getId().toString()))
                .andReturn();
    }
}