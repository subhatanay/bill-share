package com.subadev.billshare.groupbillshare.service;

import com.subadev.billshare.groupbillshare.dto.GroupExpenseSettlementResponseDTO;

public interface ExpenseSettlementService {

    public GroupExpenseSettlementResponseDTO getSettlementInfoByGroupIdAndCurrentUserId(String groupId, String currentUserId);

}
