package com.yoonho.photoresizer.controller;

import com.yoonho.photoresizer.dto.DownloadDto;
import com.yoonho.photoresizer.dto.FileDto;
import com.yoonho.photoresizer.dto.ResponseDto;
import com.yoonho.photoresizer.dto.UploadDto;
import com.yoonho.photoresizer.exception.CustomErrorPageException;
import com.yoonho.photoresizer.response.Message;
import com.yoonho.photoresizer.response.ResponseService;
import com.yoonho.photoresizer.response.StatusEnum;
import com.yoonho.photoresizer.service.FileService;
import com.yoonho.photoresizer.service.ResizeService;
import com.yoonho.photoresizer.validator.DownloadValidator;
import com.yoonho.photoresizer.validator.UploadFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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

            return responseService.getResponseEntity(new ResponseDto(HttpStatus.BAD_REQUEST, StatusEnum.BAD_REQUEST
                    , null, "지원하지 않는 확장자 입니다."));
        }

        MultipartFile multipartFile = uploadDto.getFile();
        FileDto fileDto = fileService.convertMultipartToFile(multipartFile);
        resizeService.resizeJpg(fileDto);

        return responseService.getResponseEntity(new ResponseDto(HttpStatus.OK, StatusEnum.OK
                , fileDto, null));
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@ModelAttribute @Valid DownloadDto downloadDto, BindingResult result) {
        downloadValidator.validate(downloadDto, result);

        if (result.hasErrors()) {
            log.error("다운로드 요청 데이터가 올바르지 않습니다.");

            throw new CustomErrorPageException("400");
        }

        String uuid = downloadDto.getUuid();
        String fileName = downloadDto.getFileName();
        Path path = Paths.get(resizeFilePath + "/" + uuid + "_" + fileName);
        String contentType = fileService.getContentType(path);

        return responseService.getResourceResponseEntity(fileName, contentType, path);
    }
}
