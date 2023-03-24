package com.subadev.billshare.groupbillshare.exception;

public class BadRequestException extends RuntimeException {
    public static Integer ERROR_CODE = 403;

    public BadRequestException(String message) {
        super(message);
    }
}
