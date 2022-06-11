package com.yoonho.photoresizer.scheduler;

import com.yoonho.photoresizer.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileScheduler {
    private final FileService fileService;
    @Scheduled(cron = "0 0 0/1 * * *")
    public void deleteOldFile() {
        fileService.deleteOldFile();
    }
}
