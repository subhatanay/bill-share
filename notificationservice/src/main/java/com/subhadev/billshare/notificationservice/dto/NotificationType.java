package com.subhadev.billshare.notificationservice.dto;

import java.util.Arrays;
import java.util.List;

public enum NotificationType {
    USER_ON_BOARD_SUCCESS_BY_EMAIL("WELCOME_USER_EMAIL", "OTP_EMAIL"),
    USER_OTP_EMAIL( "OTP_EMAIL"),
    USER_ON_BOARD_SUCCESS_BY_GOOGLE("WELCOME_USER_EMAIL");

    List<String> types;
    NotificationType(String ... types) {
        this.types = Arrays.asList(types);
    }

    public List<String> getTypes() {
        return types;
    }
}
