package com.subadev.billshare.groupbillshare.exception;

public class AlreadyExistsException extends RuntimeException {
    public static Integer ERROR_CODE = 409;

    public AlreadyExistsException(String message) {
        super(message);
    }
}
