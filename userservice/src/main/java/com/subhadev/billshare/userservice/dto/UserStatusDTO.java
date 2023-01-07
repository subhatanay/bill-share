package com.subhadev.billshare.userservice.dto;

import com.subhadev.billshare.userservice.entity.UserStatus;

public enum UserStatusDTO {
    CREATED,
    LOCKED,
    VERIFIED;

    public static UserStatusDTO from(UserStatus userStatus) {
        return UserStatusDTO.valueOf(userStatus.name());
    }
}
