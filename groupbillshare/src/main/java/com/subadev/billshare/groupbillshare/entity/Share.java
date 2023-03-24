package com.subadev.billshare.groupbillshare.entity;

import com.subadev.billshare.groupbillshare.dto.GroupExpensePUTRequestDTO;
import com.subadev.billshare.groupbillshare.dto.GroupExpenseRequestDTO;
import com.subadev.billshare.groupbillshare.dto.UserShare;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public  class Share implements Serializable {
    private String userId;
    private Double shareValue;

    public static Share from (UserShare s) {
        ShareBuilder builder = Share.builder().userId(s.getUserId());
        if (s.getShareValue() != null) {
            builder.shareValue(Double.valueOf(s.getShareValue()));
        }
        return builder.build();
    }

}