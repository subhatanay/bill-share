package com.subhadev.billshare.userservice.exception;

public class AuthenticationException extends RuntimeException {
    public static Integer ERROR_CODE = 401;
    public AuthenticationException(String message) {
        super(message);
    }
}
