package com.mz.identity.exception;

public class ExpiredGoogleTokenException extends RuntimeException {
    public ExpiredGoogleTokenException(String message) {
        super(message);
    }
}

