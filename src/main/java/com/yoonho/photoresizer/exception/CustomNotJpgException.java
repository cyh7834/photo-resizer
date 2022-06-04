package com.yoonho.photoresizer.exception;

public class CustomNotJpgException extends RuntimeException{
    public CustomNotJpgException() {
        super();
    }

    public CustomNotJpgException(String message) {
        super(message);
    }

    public CustomNotJpgException(String message, Throwable cause) {
        super(message, cause);
    }
}
