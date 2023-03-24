package com.subadev.billshare.groupbillshare.service;


import com.subadev.billshare.groupbillshare.dao.GroupExpenseRepository;
import com.subadev.billshare.groupbillshare.dao.GroupPaymentTransferRepository;
import com.subadev.billshare.groupbillshare.dao.GroupRepository;
import com.subadev.billshare.groupbillshare.dao.UserRepository;
import com.subadev.billshare.groupbillshare.dto.*;
import com.subadev.billshare.groupbillshare.entity.*;
import com.subadev.billshare.groupbillshare.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupPaymentTransferServiceImpl implements GroupPaymentTransferService {

    private GroupPaymentTransferRepository groupPaymentTransferRepository;
    private GroupRepository groupRepository;
    private UserRepository userRepository;
    private GroupExpenseService groupExpenseService;
    private GroupExpenseRepository expenseRepository;

    public GroupPaymentTransferServiceImpl(GroupExpenseService groupExpenseService,
                                           GroupPaymentTransferRepository groupPaymentTransferRepository,
                                           GroupRepository groupRepository,
                                           UserRepository userRepository,
                                           GroupExpenseRepository expenseRepository) {
        this.groupPaymentTransferRepository = groupPaymentTransferRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.groupExpenseService = groupExpenseService;
        this.expenseRepository = expenseRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createSettlementPaymentTransferRecord(String groupId, PaymentTransferRequestDTO paymentTransferRequestDTO) {
        paymentTransferRequestDTO.validate();
        Optional<GroupEntity> group = groupRepository.findById(groupId);
        if (!group.isPresent()) {
            throw new NotFoundException(MessageFormat.format("Group {} Not found ",groupId));
        }

        if (!groupRepository.isUserPresentBelongToGroup(groupId, paymentTransferRequestDTO.getFromUserId())) {
            throw new NotFoundException(MessageFormat.format("User {} not belong to Group {}", paymentTransferRequestDTO.getFromUserId(),groupId));
        }

        if (!groupRepository.isUserPresentBelongToGroup(groupId, paymentTransferRequestDTO.getToUserId())) {
            throw new NotFoundException(MessageFormat.format("User {} not belong to Group {}", paymentTransferRequestDTO.getToUserId(), groupId));
        }
        Optional<UserEntity> fromUser = userRepository.findById(paymentTransferRequestDTO.getFromUserId());
        Optional<UserEntity> toUser = userRepository.findById(paymentTransferRequestDTO.getToUserId());

        Map<String, Double> payee = new HashMap<>();
        payee.put(paymentTransferRequestDTO.getFromUserId(), paymentTransferRequestDTO.getAmount());

        List<UserShare> shares = new ArrayList<>();
        shares.add(UserShare.builder().userId(paymentTransferRequestDTO.getToUserId()).build());

        GroupExpenseResponseDTO paymentExpense = groupExpenseService.createExpense(groupId, GroupExpenseRequestDTO.builder()
                            .transactionDate(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()))
                            .currency(GroupExpenseRequestDTO.Currency.valueOf(paymentTransferRequestDTO.getCurrency()))
                            .description("Payment From " + paymentTransferRequestDTO.getFromUserId() + " to " + paymentTransferRequestDTO.getToUserId())
                            .shareType(GroupExpenseRequestDTO.UserShareType.EQUAL)
                            .payee(payee)
                            .shares(shares)
                        .build());

        Optional<ExpenseEntity> expenseEntity = expenseRepository.findById(paymentExpense.getExpenseId());
        PaymentTransaction paymentTransaction = PaymentTransaction.builder().paymentType(PaymentType.valueOf(paymentTransferRequestDTO.getCashMode()))
                .amount(paymentTransferRequestDTO.getAmount())
                .expenseDetails(expenseEntity.get())
                .fromUser(fromUser.get())
                .toUser(toUser.get())
                .currency(paymentTransferRequestDTO.getCurrency())
                .groupEntity(group.get())
                .build();
        paymentTransaction.setCreatedTime(Date.from(Instant.now()));
        paymentTransaction.setLastUpdatedTime(Date.from(Instant.now()));

        groupPaymentTransferRepository.save(paymentTransaction); 
    }

    @Override
    public void deleteSettlementPaymentTransferRecord(String groupId, String paymentId) {
        Optional<GroupEntity> group = groupRepository.findById(groupId);
        if (!group.isPresent()) {
            throw new NotFoundException(MessageFormat.format("Group {} Not found ",groupId));
        }

        System.out.println(paymentId + " " + groupId);
        Optional<PaymentTransaction> paymentTransaction = groupPaymentTransferRepository.findByGroupIdAndPaymentId(groupId,paymentId);
        if (!paymentTransaction.isPresent()) {
            throw new NotFoundException(MessageFormat.format("Payment {} not found under group {} ",paymentId,groupId));
        }
        groupPaymentTransferRepository.delete(paymentTransaction.get());
    }

    @Override
    public PagedResults<PaymentTransferGetResponseDTO> getPaymentTransactions(String groupId, Integer limit, Integer offset) {
        Optional<GroupEntity> group = groupRepository.findById(groupId);
        if (!group.isPresent()) {
            throw new NotFoundException(MessageFormat.format("Group {} Not found ",groupId));
        }

        Page<PaymentTransaction> paymentTransactions = groupPaymentTransferRepository.findByGroupId(groupId, Pageable.ofSize(limit).withPage(offset));
        if (paymentTransactions.getTotalElements()>0) {
           List<PaymentTransferGetResponseDTO> paymentTransferGetResponseDTOS =  paymentTransactions.getContent().stream().map(payment -> PaymentTransferGetResponseDTO.builder()
                   .fromUserId(payment.getFromUser().getUserId())
                   .toUserId(payment.getToUser().getUserId())
                   .currency(payment.getCurrency())
                   .cashMode(payment.getPaymentType().name())
                   .paymentId(payment.getPaymentId())
                   .paymentDate(payment.getCreatedTime().toString())
                   .amount(payment.getAmount())
                   .build()).collect(Collectors.toList());
           return PagedResults.<PaymentTransferGetResponseDTO>builder()
                   .results(paymentTransferGetResponseDTOS)
                   .totalCount((int) paymentTransactions.getTotalElements())
                   .pageSize(paymentTransactions.getSize())
                   .pageCount(paymentTransactions.getTotalPages())
                   .build();
        } else {
            return PagedResults.<PaymentTransferGetResponseDTO>builder()
                    .results(new ArrayList<>())
                    .totalCount((int) paymentTransactions.getTotalElements())
                    .pageSize(paymentTransactions.getSize())
                    .pageCount(paymentTransactions.getTotalPages())
                    .build();
        }

    }
}
