package com.yoonho.photoresizer.exception;

import com.yoonho.photoresizer.response.Message;
import com.yoonho.photoresizer.response.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionControllerAdvice {
    private final ResponseService responseService;

    @ExceptionHandler(CustomNotJpgException.class)
    public ResponseEntity<Message> handleCustomNotJpgException(CustomNotJpgException exception) {
        return responseService.get400ResponseEntity(null, exception.getMessage());
    }

    @ExceptionHandler(CustomIOException.class)
    public ResponseEntity<Message> handleCustomIOException(CustomIOException exception) {
        return responseService.get500ResponseEntity(null, exception.getMessage());
    }

    @ExceptionHandler(CustomImageProcessingException.class)
    public ResponseEntity<Message> handleCustomImageProcessingException(CustomImageProcessingException exception) {
        return responseService.get500ResponseEntity(null, exception.getMessage());
    }

    @ExceptionHandler(CustomMetadataException.class)
    public ResponseEntity<Message> handleCustomMetadataException(CustomMetadataException exception) {
        return responseService.get500ResponseEntity(null, exception.getMessage());
    }

    @ExceptionHandler(CustomErrorPageException.class)
    public String getErrorPage(CustomErrorPageException customErrorPageException) {
        log.error("error", customErrorPageException);

        return "error/" + customErrorPageException.getMessage() + "error";
    }
}
