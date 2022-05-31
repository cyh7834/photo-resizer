package com.yoonho.photoresizer.exception;

public class CustomImageProcessingException extends RuntimeException{
    public CustomImageProcessingException() {
        super();
    }

    public CustomImageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
