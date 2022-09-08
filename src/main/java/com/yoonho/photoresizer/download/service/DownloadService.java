package com.yoonho.photoresizer.download.service;

import com.yoonho.photoresizer.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DownloadService {
    private final PhotoService photoService;

    public String getResizeFilePath(String uuid) {
        return photoService.findResizePathByUUID(uuid);
    }
}
