package com.subadev.billshare.groupbillshare.service;

import com.subadev.billshare.groupbillshare.dto.PagedResults;
import com.subadev.billshare.groupbillshare.dto.PaymentTransferGetResponseDTO;
import com.subadev.billshare.groupbillshare.dto.PaymentTransferRequestDTO;

public interface GroupPaymentTransferService {

    public void createSettlementPaymentTransferRecord(String groupId, PaymentTransferRequestDTO paymentTransferRequestDTO);

    public void deleteSettlementPaymentTransferRecord(String groupId, String expenseId);

     PagedResults<PaymentTransferGetResponseDTO> getPaymentTransactions(String groupId, Integer limit, Integer offset);

}
