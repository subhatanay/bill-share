package com.subadev.billshare.groupbillshare.dto;

import com.subadev.billshare.groupbillshare.entity.Share;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public  class UserShare {
    private String userId;
    private Double shareValue;

    public static UserShare from (Share s) {
        return UserShare.builder().shareValue(s.getShareValue()).userId(s.getUserId()).build();
    }
}
