package com.yoonho.photoresizer.dto;

import com.yoonho.photoresizer.response.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ResponseDto {
    private HttpStatus httpStatus;

    private StatusEnum statusEnum;

    private Object data;

    private String comment;
}
