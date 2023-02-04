package com.subadev.billshare.groupbillshare.exception;

public class InternalServerException extends RuntimeException {
    public static Integer ERROR_CODE = 500;

    public InternalServerException(String message) {
        super(message);
    }
}
