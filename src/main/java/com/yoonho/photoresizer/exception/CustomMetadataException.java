package com.yoonho.photoresizer.exception;

public class CustomMetadataException extends RuntimeException{
    public CustomMetadataException() {
        super();
    }

    public CustomMetadataException(String message, Throwable cause) {
        super(message, cause);
    }
}
