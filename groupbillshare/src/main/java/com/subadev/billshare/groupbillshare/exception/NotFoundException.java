package com.subadev.billshare.groupbillshare.exception;

public class NotFoundException extends RuntimeException {

    public static Integer ERROR_CODE = 404;

    public NotFoundException(String message) {
        super(message);
    }
}
