package com.exchangediary.diary.ui.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
public class UploadImageRequest {
    private MultipartFile file;
}
