package com.yoonho.photoresizer.exception;

public class CustomAlreadySquareImageException extends RuntimeException{
    public CustomAlreadySquareImageException() {
        super();
    }

    public CustomAlreadySquareImageException(String message) {
        super(message);
    }
}
