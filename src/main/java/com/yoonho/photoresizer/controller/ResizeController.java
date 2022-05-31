package com.yoonho.photoresizer.controller;

import com.yoonho.photoresizer.dto.DownloadDto;
import com.yoonho.photoresizer.dto.FileDto;
import com.yoonho.photoresizer.dto.UploadDto;
import com.yoonho.photoresizer.exception.CustomIOException;
import com.yoonho.photoresizer.service.FileService;
import com.yoonho.photoresizer.service.ResizeService;
import com.yoonho.photoresizer.validator.UploadFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ResizeController {
    private final ResizeService resizeService;
    private final FileService fileService;
    private final UploadFormValidator uploadFormValidator;

    @Value("${resize.file.path}")
    private String resizeFilePath;

    @GetMapping("/")
    public String uploadPage(Model model) {
        model.addAttribute("uploadDto", new UploadDto());

        return "upload";
    }

    @PostMapping("/resize")
    public String resizeJpg(UploadDto uploadDto, BindingResult result, Model model) {
        uploadFormValidator.validate(uploadDto, result);

        if (result.hasErrors()) {
            model.addAttribute(uploadDto);
            log.error("파일 확장자 에러 발생");

            return "upload";
        }

        List<MultipartFile> multipartFiles = uploadDto.getFiles();
        List<FileDto> files = fileService.convertMultipartToFile(multipartFiles);
        List<DownloadDto> downloadDtoList = resizeService.resizeJpg(files);

        model.addAttribute("files", downloadDtoList);

        return "download";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@ModelAttribute DownloadDto downloadDto) {
        Path path = Paths.get(resizeFilePath + "/" + downloadDto.getUuid() + "_" + downloadDto.getFileName());
        String contentType = null;

        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            throw new CustomIOException("파일 다운로드 중 확장자 오류가 발생하였습니다.", e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(downloadDto.getFileName(), StandardCharsets.UTF_8).build());
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        Resource resource = null;

        try {
            resource = new InputStreamResource(Files.newInputStream(path));
        } catch (IOException e) {
            throw new CustomIOException("파일 다운로드 중 오류가 발생하였습니다.", e);
        }

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
