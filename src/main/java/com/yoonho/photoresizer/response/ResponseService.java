package com.yoonho.photoresizer.response;

import com.yoonho.photoresizer.response.dto.ResponseDto;
import com.yoonho.photoresizer.exception.CustomErrorPageException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ResponseService {
    public ResponseEntity<Message> getResponseEntity(ResponseDto responseDto) {
        HttpHeaders headers = getHeader();
        Message message = getMessage(responseDto.getData(), responseDto.getComment(), responseDto.getStatusEnum());

        return new ResponseEntity<>(message, headers, responseDto.getHttpStatus());
    }

    public ResponseEntity<Resource> getResourceResponseEntity(String fileName, String contentType, Path path) {
        HttpHeaders headers = getHeader(fileName, contentType);
        Resource resource = null;

        try {
            resource = new InputStreamResource(Files.newInputStream(path));

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new CustomErrorPageException("500", e);
        }
    }

    private HttpHeaders getHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return headers;
    }

    private HttpHeaders getHeader(String fileName, String contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("resized_" + fileName, StandardCharsets.UTF_8).build());
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        return headers;
    }

    private Message getMessage(Object data, String comment, StatusEnum statusEnum) {
        Message message = new Message();
        message.setStatus(statusEnum);
        message.setComment(comment);
        message.setData(data);

        return message;
    }
}
