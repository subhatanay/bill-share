package com.subadev.billshare.groupbillshare.service.settlement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExpenseSettlementFactory {

    @Value("${app.settlement-strategy:per-transaction}")
    String settlementStrategy;

    public ExpenseSettlement getExpenseSettlement() {
        switch (settlementStrategy) {
            case "per-transaction":
                return new PerTransactionExpenseSettlement();
            default:
                return null;
        }
    }


}
