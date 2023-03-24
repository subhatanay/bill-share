package com.subadev.billshare.groupbillshare.service.settlement;

import com.subadev.billshare.groupbillshare.entity.ExpenseEntity;

import java.util.List;
import java.util.Map;

public abstract class ExpenseSettlement {

    public abstract Map<String, Map<String,Double>> getSettlementDetails(List<ExpenseEntity> transactions);

}
