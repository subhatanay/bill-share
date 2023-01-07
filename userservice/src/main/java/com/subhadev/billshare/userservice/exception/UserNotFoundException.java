package com.subhadev.billshare.userservice.exception;

public class UserNotFoundException extends RuntimeException {
    public static Integer ERROR_CODE = 400;

    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
