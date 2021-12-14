package com.example.reddit.exception;

public class UnknownException extends RuntimeException {
    public UnknownException(String message) {
        super(message);
    }
}
