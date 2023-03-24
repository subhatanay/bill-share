package com.subadev.billshare.groupbillshare.dto;

import com.subadev.billshare.groupbillshare.entity.Share;
import com.subadev.billshare.groupbillshare.exception.ValidationException;
import lombok.Builder;

import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
public class GroupExpenseRequestDTO implements Validation {
    private String expenseName;
    private String description;
    private String transactionDate;

    private Double amount;
    private Currency currency;

    private Map<String, Double> payee;
    private UserShareType shareType;
    private List<UserShare> shares;

    @Override
    public void validate() throws ValidationException {
        if (expenseName==null || expenseName.trim().isEmpty()) {
            throw new ValidationException("Expense name can't be null or empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new ValidationException("Expense Description can't be null or empty");
        }
        if (transactionDate == null) {
            throw new ValidationException("Expense date can't be null");
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY");
            simpleDateFormat.parse(transactionDate);
        } catch (ParseException parseException) {
            throw new ValidationException("Expense transaction date can't be parsed. Please add date in dd-MM-YYYY format.");
        }
        if (amount == null || amount < 0) {
            throw new ValidationException("Expense Amount can't be null or less then 0");
        }
        if (currency == null) {
            throw new ValidationException("Current can't be null. RUPEE , DOLLAR are supported values");
        }

        if (payee == null || payee.size() == 0) {
            throw new ValidationException("Atleast Payee information is required");
        }
        payee.entrySet().forEach(pay -> {
            UUID.fromString(pay.getKey());
            if (pay.getValue() == null || pay.getValue() < 0) {
                throw new ValidationException("Expense Payee Amount can't be null or less then 0");
            }
        });
        if (shareType == null) {
            throw new ValidationException("Expense Share Type can't be null. EQUAL , UNEQUAL, PERCENT are supported values");
        }
        shares.forEach(share -> {
            UUID.fromString(share.getUserId());
            if (shareType == UserShareType.UNEQUAL) {
                if (share.getShareValue() == null || share.getShareValue() < 0) {
                    throw new ValidationException("UNEQUAL Share Type - Expense Shared Amount can't be null or less then 0");
                }
            }

            if (shareType == UserShareType.PERCENT) {
                if (share.getShareValue() == null || share.getShareValue() < 0) {
                    throw new ValidationException("Percent Share Type - Expense Shared Value should be between 0 to 100 percent.");
                }
            }

            if (shareType == UserShareType.EQUAL) {
                if (share.getShareValue() !=null) {
                    throw new ValidationException("Equal Share Type - Expense Shared Value should not be provided.");
                }
            }
        });
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
