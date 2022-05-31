package com.yoonho.photoresizer.exception;

public class CustomIOException extends RuntimeException{
    public CustomIOException() {
        super();
    }

    public CustomIOException(String message) {
        super(message);
    }

    public CustomIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
