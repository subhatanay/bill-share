package com.subhadev.billshare.userservice.entity;

import com.subhadev.billshare.userservice.dto.UserStatusDTO;

public enum UserStatus {
    VERIFIED, CREATED, LOCKED;

    public static UserStatus from(UserStatusDTO userStatusDTO) {
        return UserStatus.valueOf(userStatusDTO.name());
    }
}
