package com.exchangediary.diary.service;

import com.exchangediary.diary.domain.DiaryRepository;
import com.exchangediary.diary.domain.entity.Diary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryDeleteService {
    private final ImageService imageService;
    private final DiaryRepository diaryRepository;

    public void deleteDiary(String groupId, Long memberId) {
        List<Diary> diaries = diaryRepository.findByMemberId(memberId);

        diaryRepository.deleteByMemberId(memberId);

        diaries.forEach(diary -> {
            if (diary.getImageFileName() != null) {
                imageService.deleteImage(groupId, diary.getImageFileName());
            }
        });
    }
}
