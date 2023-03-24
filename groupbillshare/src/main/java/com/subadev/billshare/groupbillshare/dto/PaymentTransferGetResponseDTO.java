package com.subadev.billshare.groupbillshare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTransferGetResponseDTO {
    private String paymentId;
    private String fromUserId;
    private String toUserId;
    private String currency;
    private Double amount;
    private String cashMode;
    private String paymentDate;
}
