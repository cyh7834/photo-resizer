package com.yoonho.photoresizer.response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class ResponseService {
    public ResponseEntity<Message> get200ResponseEntity(Object data, String comment) {
        HttpHeaders headers = getHeader();
        Message message = get200Message(data, comment);

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    public ResponseEntity<Message> get400ResponseEntity(Object data, String comment) {
        HttpHeaders headers = getHeader();
        Message message = get400Message(data, comment);

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    public ResponseEntity<Message> get500ResponseEntity(Object data, String comment) {
        HttpHeaders headers = getHeader();
        Message message = get500Message(data, comment);

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    private HttpHeaders getHeader() {
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return headers;
    }

    private Message get200Message(Object data, String comment) {
        Message message = new Message();
        message.setStatus(StatusEnum.OK);
        message.setComment(comment);
        message.setData(data);

        return message;
    }

    private Message get400Message(Object data, String comment) {
        Message message = new Message();
        message.setStatus(StatusEnum.BAD_REQUEST);
        message.setComment(comment);
        message.setData(data);

        return message;
    }

    private Message get500Message(Object data, String comment) {
        Message message = new Message();
        message.setStatus(StatusEnum.INTERNAL_SERVER_ERROR);
        message.setComment(comment);
        message.setData(data);

        return message;
    }
}
