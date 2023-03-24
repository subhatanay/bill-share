package com.subadev.billshare.groupbillshare.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class GroupExpenseGetResponseDTO {
    private String expenseId;
    private String expenseName;
    private String description;
    private String transactionDate;

    private Double amount;
    private GroupExpenseRequestDTO.Currency currency;

    private GroupExpenseRequestDTO.UserShareType shareType;
    private Map<String, Double> payee;
    private List< ? extends UserShare> shares;
}
