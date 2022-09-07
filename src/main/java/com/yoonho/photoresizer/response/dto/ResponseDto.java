package com.yoonho.photoresizer.response.dto;

import com.yoonho.photoresizer.response.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@ToString
public class ResponseDto {
    private HttpStatus httpStatus;

    private StatusEnum statusEnum;

    private Object data;

    private String comment;
}
