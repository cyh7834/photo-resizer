package com.yoonho.photoresizer.resize.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UploadDto {
    private MultipartFile file;
}
