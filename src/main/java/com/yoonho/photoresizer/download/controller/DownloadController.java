package com.yoonho.photoresizer.download.controller;

import com.yoonho.photoresizer.download.dto.DownloadDto;
import com.yoonho.photoresizer.exception.CustomErrorPageException;
import com.yoonho.photoresizer.file.service.FileService;
import com.yoonho.photoresizer.response.ResponseService;
import com.yoonho.photoresizer.validator.DownloadValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DownloadController {
    private final DownloadValidator downloadValidator;
    private final ResponseService responseService;
    private final FileService fileService;

    @Value("${resize.file.path}")
    private String resizeFilePath;

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@ModelAttribute @Valid DownloadDto downloadDto, BindingResult result) {
        downloadValidator.validate(downloadDto, result);

        if (result.hasErrors()) {
            throw new CustomErrorPageException("400");
        }

        String uuid = downloadDto.getUuid();
        String fileName = downloadDto.getFileName();
        Path path = Paths.get(resizeFilePath + "/" + uuid + "_" + fileName);
        String contentType = fileService.getContentType(path);

        return responseService.getResourceResponseEntity(fileName, contentType, path);
    }
}
