package com.yurakim.readingtrace.auth.exception;

public class RefreshTokenException extends RuntimeException {

    private String code;

    public RefreshTokenException(String code, String message) {
        super(message); // sets RuntimeException message
        this.code = code;
    }

}