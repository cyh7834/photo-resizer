package com.yoonho.photoresizer.controller;

import com.yoonho.photoresizer.dto.DownloadDto;
import com.yoonho.photoresizer.dto.FileDto;
import com.yoonho.photoresizer.dto.UploadDto;
import com.yoonho.photoresizer.exception.CustomIOException;
import com.yoonho.photoresizer.response.Message;
import com.yoonho.photoresizer.response.ResponseService;
import com.yoonho.photoresizer.service.FileService;
import com.yoonho.photoresizer.service.ResizeService;
import com.yoonho.photoresizer.validator.DownloadValidator;
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

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ResizeController {
    private final ResizeService resizeService;
    private final FileService fileService;
    private final UploadFormValidator uploadFormValidator;
    private final DownloadValidator downloadValidator;
    private final ResponseService responseService;

    @Value("${resize.file.path}")
    private String resizeFilePath;

    @GetMapping("/")
    public String uploadPage(Model model) {
        model.addAttribute("uploadDto", new UploadDto());

        return "upload";
    }

    @PostMapping("/resize")
    @ResponseBody
    public ResponseEntity<Message> resizeJpg(UploadDto uploadDto, BindingResult result) {
        uploadFormValidator.validate(uploadDto, result);

        if (result.hasErrors()) {
            log.error("파일 확장자 에러 발생");

            return responseService.get400ResponseEntity(null, "지원하지 않는 확장자 입니다.");
        }

        MultipartFile multipartFile = uploadDto.getFile();
        FileDto fileDto = fileService.convertMultipartToFile(multipartFile);
        resizeService.resizeJpg(fileDto);

        return responseService.get200ResponseEntity(fileDto, null);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@ModelAttribute @Valid DownloadDto downloadDto, BindingResult result) {
        downloadValidator.validate(downloadDto, result);

        if (result.hasErrors()) {
            log.error("파일 다운로드 에러 발생");

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String uuid = downloadDto.getUuid();
        String fileName = downloadDto.getFileName();
        Path path = Paths.get(resizeFilePath + "/" + uuid + "_" + fileName);
        String contentType = null;

        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            throw new CustomIOException("파일 다운로드 중 확장자 오류가 발생하였습니다.", e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("resized_" + fileName, StandardCharsets.UTF_8).build());
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
