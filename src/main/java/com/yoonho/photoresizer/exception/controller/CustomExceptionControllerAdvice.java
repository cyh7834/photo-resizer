package com.yoonho.photoresizer.exception.controller;

import com.yoonho.photoresizer.response.dto.ResponseDto;
import com.yoonho.photoresizer.exception.*;
import com.yoonho.photoresizer.response.Message;
import com.yoonho.photoresizer.response.ResponseService;
import com.yoonho.photoresizer.response.StatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionControllerAdvice {
    private final ResponseService responseService;

    @ExceptionHandler(CustomNotJpgException.class)
    public ResponseEntity<Message> handleCustomNotJpgException(CustomNotJpgException exception) {
        log.error("CustomNotJpgException", exception);

        return responseService.getResponseEntity(new ResponseDto(HttpStatus.BAD_REQUEST, StatusEnum.BAD_REQUEST
                , null, exception.getMessage()));
    }

    @ExceptionHandler(CustomIOException.class)
    public ResponseEntity<Message> handleCustomIOException(CustomIOException exception) {
        log.error("CustomIOException", exception);

        return responseService.getResponseEntity(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR
                , StatusEnum.INTERNAL_SERVER_ERROR, null, exception.getMessage()));
    }

    @ExceptionHandler(CustomImageProcessingException.class)
    public ResponseEntity<Message> handleCustomImageProcessingException(CustomImageProcessingException exception) {
        log.error("CustomImageProcessingException", exception);

        return responseService.getResponseEntity(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR
                , StatusEnum.INTERNAL_SERVER_ERROR, null, exception.getMessage()));
    }

    @ExceptionHandler(CustomMetadataException.class)
    public ResponseEntity<Message> handleCustomMetadataException(CustomMetadataException exception) {
        log.error("CustomMetadataException", exception);

        return responseService.getResponseEntity(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR
                , StatusEnum.INTERNAL_SERVER_ERROR, null, exception.getMessage()));
    }

    @ExceptionHandler(CustomErrorPageException.class)
    public String getErrorPage(CustomErrorPageException customErrorPageException, Model model) {
        log.error("error", customErrorPageException);

        String message = customErrorPageException.getMessage();

        if (message.equals("400")) {
            model.addAttribute("code", 400);
            model.addAttribute("message", "Not a valid request.");
        }
        else if (message.equals("500")) {
            model.addAttribute("code", 500);
            model.addAttribute("message", "Internal server error.");
        }

        return "error/error";
    }
}
