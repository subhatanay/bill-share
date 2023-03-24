package com.subadev.billshare.groupbillshare.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GroupExpenseSettlementResponseDTO {

    private List<UserSettlementInfo> settlementList;

    @Data
    @Builder
    public static class UserSettlementInfo {
        private String userId;
        private Double amount;
    }
}
