package com.subadev.billshare.groupbillshare.controller;

import com.subadev.billshare.groupbillshare.dto.PagedResults;
import com.subadev.billshare.groupbillshare.dto.PaymentTransferGetResponseDTO;
import com.subadev.billshare.groupbillshare.dto.PaymentTransferRequestDTO;
import com.subadev.billshare.groupbillshare.service.GroupPaymentTransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups/{groupId}/payments")
public class GroupPaymentTransferController {
    GroupPaymentTransferService groupPaymentTransferService;

    public GroupPaymentTransferController(GroupPaymentTransferService groupPaymentTransferService) {
        this.groupPaymentTransferService = groupPaymentTransferService;
    }

    @PostMapping
    public ResponseEntity createSettlementPayment(@PathVariable("groupId") String groupId, @RequestBody PaymentTransferRequestDTO paymentTransferRequestDTO) {
        groupPaymentTransferService.createSettlementPaymentTransferRecord(groupId, paymentTransferRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(path = "/{paymentId}")
    public ResponseEntity deleteSettlementPayment(@PathVariable("groupId") String groupId, @PathVariable("paymentId") String paymentId) {
        groupPaymentTransferService.deleteSettlementPaymentTransferRecord(groupId, paymentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity fetchSettlementPayments(@PathVariable("groupId") String groupId,
                                                  @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                                  @RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        PagedResults<PaymentTransferGetResponseDTO> paymentTransferGetResponseDTOPagedResults = groupPaymentTransferService.getPaymentTransactions(groupId, limit, offset);
        return ResponseEntity.status(HttpStatus.OK).body(paymentTransferGetResponseDTOPagedResults);
    }

}
