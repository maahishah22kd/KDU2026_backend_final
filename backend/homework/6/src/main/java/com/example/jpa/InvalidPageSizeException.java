package com.example.jpa;

public class InvalidPageSizeException extends RuntimeException {

    public InvalidPageSizeException(String message) {
        super(message);
    }
}
