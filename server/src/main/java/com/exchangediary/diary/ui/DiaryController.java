package com.exchangediary.diary.ui;

import com.exchangediary.diary.domain.entity.Diary;
import com.exchangediary.diary.service.DiaryCommandService;
import com.exchangediary.diary.service.DiaryQueryService;
import com.exchangediary.diary.service.StickerCommandService;
import com.exchangediary.diary.ui.dto.request.DiaryRequest;
import com.exchangediary.diary.ui.dto.request.StickerRequest;
import com.exchangediary.diary.ui.dto.request.UploadImageRequest;
import com.exchangediary.diary.ui.dto.response.DiaryDetailResponse;
import com.exchangediary.diary.ui.dto.response.DiaryMonthlyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {
    private final DiaryCommandService diaryCommandService;
    private final StickerCommandService stickerCommandService;
    private final DiaryQueryService diaryQueryService;

    @PostMapping
    public ResponseEntity<Long> createDiary(
            @RequestPart(name = "data") @Valid DiaryRequest diaryRequest,
            @RequestPart(name = "file", required = false) MultipartFile file) {
        UploadImageRequest uploadImageRequest = UploadImageRequest.builder()
                .file(file)
                .build();
        Diary diary = diaryCommandService.createDiary(diaryRequest, uploadImageRequest);
        return ResponseEntity
                .created(URI.create(String.format("/diary/%d/", diary.getId())))
                .body(diary.getId());
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryDetailResponse> viewDetail (@PathVariable Long diaryId) {
        DiaryDetailResponse diaryDetailResponse = diaryQueryService.viewDetail(diaryId);

        return ResponseEntity
                .ok()
                .body(diaryDetailResponse);
    }

    @PostMapping("/{diaryId}/sticker/{stickerId}")
    public ResponseEntity<Void> createSticker(
            @RequestBody @Valid StickerRequest stickerRequest,
            @PathVariable Long diaryId,
            @PathVariable Long stickerId
    ) {
        stickerCommandService.createSticker(stickerRequest, diaryId, stickerId);
        return ResponseEntity
                .created(URI.create("/diary/" + diaryId))
                .build();
    }

    @GetMapping("/monthly")
    public ResponseEntity<DiaryMonthlyResponse> viewMonthlyDiary(@RequestParam int year, @RequestParam int month) {
        DiaryMonthlyResponse diaryMonthlyResponse = diaryQueryService.viewMonthlyDiary(year, month);

        return ResponseEntity
                .ok()
                .body(diaryMonthlyResponse);
    }
}
