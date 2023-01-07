package com.subhadev.billshare.userservice.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public static Integer ERROR_CODE = 409;

    public UserAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
