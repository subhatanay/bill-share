package com.subadev.billshare.groupbillshare.exception;

public class ForbiddenException extends RuntimeException {
    public static Integer ERROR_CODE = 403;

    public ForbiddenException(String message) {
        super(message);
    }
}
