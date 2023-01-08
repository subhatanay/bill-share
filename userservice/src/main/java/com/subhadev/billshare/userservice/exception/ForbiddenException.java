package com.subhadev.billshare.userservice.exception;

public class ForbiddenException extends RuntimeException {
    public static Integer ERROR_CODE = 403;

    public ForbiddenException(String errorMessage) {
        super(errorMessage);
    }
}
