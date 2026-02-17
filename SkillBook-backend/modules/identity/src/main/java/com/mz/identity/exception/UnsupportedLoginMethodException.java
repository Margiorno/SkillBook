package com.mz.identity.exception;

public class UnsupportedLoginMethodException extends RuntimeException{
    public UnsupportedLoginMethodException(String message) {
        super(message);
    }
}
