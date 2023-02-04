package com.subadev.billshare.groupbillshare.exception;

public class ValidationException extends RuntimeException {
    public static Integer ERROR_CODE = 400;

    public ValidationException(String errorMessage) {
        super(errorMessage);
    }

}
