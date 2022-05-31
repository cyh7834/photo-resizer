package com.yoonho.photoresizer.exception;

import com.yoonho.photoresizer.dto.UploadDto;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionControllerAdvice {
    @ExceptionHandler(CustomNotJpgException.class)
    public String handleCustomNotJpgException(CustomNotJpgException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("uploadDto", new UploadDto());

        return "upload";
    }

    @ExceptionHandler(CustomIOException.class)
    public String handleCustomIOException(CustomIOException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("uploadDto", new UploadDto());

        return "upload";
    }

    @ExceptionHandler(CustomImageProcessingException.class)
    public String handleCustomImageProcessingException(CustomImageProcessingException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("uploadDto", new UploadDto());

        return "upload";
    }

    @ExceptionHandler(CustomMetadataException.class)
    public String handleCustomMetadataException(CustomMetadataException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        model.addAttribute("uploadDto", new UploadDto());

        return "upload";
    }
}
