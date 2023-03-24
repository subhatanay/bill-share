package com.subadev.billshare.groupbillshare.dto;


import com.subadev.billshare.groupbillshare.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTransferRequestDTO implements Validation {
    private String fromUserId;
    private String toUserId;
    private String currency;
    private Double amount;
    private String cashMode;

    @Override
    public void validate() throws ValidationException {
        if (fromUserId == null || fromUserId.length() == 0) {
            throw new ValidationException("From User Id should not be null or empty");
        }
        if (toUserId == null || toUserId.length() == 0) {
            throw new ValidationException("To User Id should not be null or empty");
        }
        if (currency == null || currency.length() == 0) {
            throw new ValidationException("Currency should not be null or empty");
        }
        if (!currency.equals("RUPEE")) {
            throw new ValidationException("Currently only RUPEE currency supported");
        }

        if (amount == null || amount <=0) {
            throw new ValidationException("Amount should not be null or less than equal to 0");
        }

        if (cashMode == null || cashMode.length() == 0) {
            throw new ValidationException("Currency should not be null or empty");
        }
        if (!cashMode.equals("CASH")) {
            throw new ValidationException("Currently only CASH mode of payments are allowed");
        }
     }
}
