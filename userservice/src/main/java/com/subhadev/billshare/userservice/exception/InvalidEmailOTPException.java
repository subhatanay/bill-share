package com.subhadev.billshare.userservice.exception;

public class InvalidEmailOTPException extends RuntimeException {
    public static Integer ERROR_CODE = 400;

    public InvalidEmailOTPException(String errorMessage) {
        super(errorMessage);
    }
}
