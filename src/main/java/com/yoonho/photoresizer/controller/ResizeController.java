package com.yoonho.photoresizer.controller;

import com.yoonho.photoresizer.dto.DownloadDto;
import com.yoonho.photoresizer.dto.FileDto;
import com.yoonho.photoresizer.service.FileService;
import com.yoonho.photoresizer.service.ResizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ResizeController {
    private final ResizeService resizeService;
    private final FileService fileService;

    @Value("${resize.file.path}")
    private String resizeFilePath;

    @GetMapping("/")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping("/resize")
    public String resizeJpg(@RequestParam("files") List<MultipartFile> multipartFiles, Model model) {
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
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(downloadDto.getFileName(), StandardCharsets.UTF_8).build());
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        Resource resource = null;

        try {
            resource = new InputStreamResource(Files.newInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
