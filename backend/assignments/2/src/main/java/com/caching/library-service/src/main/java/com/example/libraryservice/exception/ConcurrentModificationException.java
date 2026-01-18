package com.example.libraryservice.exception;

public class ConcurrentModificationException extends RuntimeException {
    public ConcurrentModificationException(String message) { super(message); }
}
