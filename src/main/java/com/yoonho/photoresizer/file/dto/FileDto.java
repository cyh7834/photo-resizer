package com.yoonho.photoresizer.file.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileDto {
    private String uuid;
    private String fileName;
    private String fileSize;
}
