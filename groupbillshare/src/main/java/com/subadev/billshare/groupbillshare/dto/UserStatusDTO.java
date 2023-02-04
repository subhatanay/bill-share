package com.subadev.billshare.groupbillshare.dto;

import com.subadev.billshare.groupbillshare.entity.UserStatus;

public enum UserStatusDTO {
    CREATED,
    LOCKED,
    VERIFIED;

    public static UserStatusDTO from(UserStatus userStatus) {
        return UserStatusDTO.valueOf(userStatus.name());
    }
}
