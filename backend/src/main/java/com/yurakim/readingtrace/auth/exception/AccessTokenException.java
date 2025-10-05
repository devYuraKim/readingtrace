package com.yurakim.readingtrace.auth.exception;

import lombok.Getter;

@Getter
public class AccessTokenException extends RuntimeException {

    private String code;

    public AccessTokenException(String code, String message) {
        super(message); // sets RuntimeException message
        this.code = code;
    }

}