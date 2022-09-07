package com.yoonho.photoresizer.resize.controller;

import com.yoonho.photoresizer.file.dto.FileDto;
import com.yoonho.photoresizer.response.dto.ResponseDto;
import com.yoonho.photoresizer.resize.dto.UploadDto;
import com.yoonho.photoresizer.file.service.FileService;
import com.yoonho.photoresizer.resize.service.ResizeService;
import com.yoonho.photoresizer.response.Message;
import com.yoonho.photoresizer.response.ResponseService;
import com.yoonho.photoresizer.response.StatusEnum;
import com.yoonho.photoresizer.validator.UploadFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ResizeController {
    private final ResizeService resizeService;
    private final FileService fileService;
    private final UploadFormValidator uploadFormValidator;
    private final ResponseService responseService;

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
            StringBuilder message = new StringBuilder();
            List<ObjectError> allErrors = result.getAllErrors();

            for (ObjectError error : allErrors) {
                message.append(error.getCode());
                message.append("\n");
            }

            return responseService.getResponseEntity(new ResponseDto(HttpStatus.BAD_REQUEST, StatusEnum.BAD_REQUEST
                    , null, message.toString()));
        }

        MultipartFile multipartFile = uploadDto.getFile();
        FileDto fileDto = fileService.convertMultipartToFile(multipartFile);

        resizeService.resizeJpg(fileDto);

        return responseService.getResponseEntity(new ResponseDto(HttpStatus.OK, StatusEnum.OK
                , fileDto, null));
    }
}
