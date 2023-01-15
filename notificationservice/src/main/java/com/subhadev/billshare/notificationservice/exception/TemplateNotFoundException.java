package com.subhadev.billshare.notificationservice.exception;

public class TemplateNotFoundException extends  RuntimeException {

    public static String ERROR_CODE = "400";

    public TemplateNotFoundException(String message) {
        super(message);
    }

}
