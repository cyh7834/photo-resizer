package com.yoonho.photoresizer.exception;

public class CustomErrorPageException extends RuntimeException{
    public CustomErrorPageException() {
        super();
    }

    public CustomErrorPageException(String message) {
        super(message);
    }

    public CustomErrorPageException(String message, Throwable cause) {
        super(message, cause);
    }
}
