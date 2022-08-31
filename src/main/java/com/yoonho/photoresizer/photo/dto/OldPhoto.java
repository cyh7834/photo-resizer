package com.yoonho.photoresizer.photo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OldPhoto {
    private Long id;

    private String uploadPath;

    private String resizePath;
}
