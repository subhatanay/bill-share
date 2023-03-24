package com.subadev.billshare.groupbillshare.dto;

import com.subadev.billshare.groupbillshare.entity.SettlementDetails;
import com.subadev.billshare.groupbillshare.exception.ValidationException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class GroupExpensePUTRequestDTO implements Validation {
    private String expenseName;
    private String description;
    private String  transactionDate;

    private Double amount;
    private Currency currency;

    private Map<String, Double> payee;
    private UserShareType shareType;
    private List<UserShare > shares;

    @Override
    public void validate() throws ValidationException {

    }
    public enum Currency {
        RUPEE,
        DOLLAR
    }

    public enum UserShareType {
        EQUAL,
        UNEQUAL,
        PERCENT
    }
}
