package com.yoonho.photoresizer.dto;

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
