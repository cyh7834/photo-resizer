package com.yoonho.photoresizer.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DownloadDto {
    private String uuid;
    private String fileName;
}
