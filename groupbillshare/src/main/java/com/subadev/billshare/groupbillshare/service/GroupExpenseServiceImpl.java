package com.subadev.billshare.groupbillshare.service;

import com.subadev.billshare.groupbillshare.dao.GroupExpenseRepository;
import com.subadev.billshare.groupbillshare.dao.GroupRepository;
import com.subadev.billshare.groupbillshare.dao.GroupUsersRepository;
import com.subadev.billshare.groupbillshare.dto.*;
import com.subadev.billshare.groupbillshare.entity.*;
import com.subadev.billshare.groupbillshare.exception.BadRequestException;
import com.subadev.billshare.groupbillshare.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupExpenseServiceImpl implements GroupExpenseService {
    private GroupRepository groupRepository;
    private GroupExpenseRepository groupExpenseRepository;
    private GroupUsersRepository groupUsersRepository;


    public GroupExpenseServiceImpl(GroupRepository groupRepository, GroupExpenseRepository groupExpenseRepository, GroupUsersRepository groupUsersRepository) {
        this.groupRepository = groupRepository;
        this.groupExpenseRepository = groupExpenseRepository;
        this.groupUsersRepository = groupUsersRepository;
    }

    @Override
    public GroupExpenseResponseDTO createExpense(String groupId, GroupExpenseRequestDTO groupExpenseRequestDTO) {
        Optional<GroupEntity> groupEntity = this.groupRepository.findById(groupId);
        if (!groupEntity.isPresent()) {
            throw new NotFoundException("Group not found");
        }
        List<String> payeeUsers = groupExpenseRequestDTO.getPayee().keySet().stream().collect(Collectors.toList());
        if (groupUsersRepository.countUsersUnderGroup(groupId,payeeUsers)  != payeeUsers.size()) {
            throw new BadRequestException("Payee users does not belong to requested group " + groupId + ". Please re-verify payee users details.");
        }
        List<String> sharedUsers = groupExpenseRequestDTO.getShares().stream().map(share -> share.getUserId()).collect(Collectors.toList());
        if (groupUsersRepository.countUsersUnderGroup(groupId,sharedUsers)  != sharedUsers.size()) {
            throw new BadRequestException("Shared users does not belong to requested group " + groupId + ". Please re-verify shared users details.");
        }
        ExpenseEntity expenseEntity = ExpenseEntity.builder()
                                    .expenseName(groupExpenseRequestDTO.getExpenseName())
                                    .description(groupExpenseRequestDTO.getDescription())
                                    .expenseDate(Date.valueOf(groupExpenseRequestDTO.getTransactionDate()))
                                    .amount(groupExpenseRequestDTO.getAmount())
                                    .currency(groupExpenseRequestDTO.getCurrency().name())
                                    .expenseType(ExpenseType.valueOf(groupExpenseRequestDTO.getShareType().name()))
                                    .expenseDetails(buildExpenseDetailsObject(groupExpenseRequestDTO))
                                    .group(groupEntity.get())


                                    .build();

        expenseEntity.setCreatedTime(Date.from(Instant.now()));
        expenseEntity.setLastUpdatedTime(Date.from(Instant.now()));

        expenseEntity = this.groupExpenseRepository.save(expenseEntity);
        return GroupExpenseResponseDTO.builder().expenseId(expenseEntity.getExpenseId()).build();
    }

    private SettlementDetails buildExpenseDetailsObject(GroupExpenseRequestDTO groupExpenseRequestDTO) {
        SettlementDetails expenseDetails = new SettlementDetails();


        expenseDetails.setPayee(groupExpenseRequestDTO.getPayee());
        expenseDetails.setShares( groupExpenseRequestDTO.getShares().stream().map(val -> Share.from(val)).collect(Collectors.toList()));

        return expenseDetails;
    }

    @Override
    public PagedResults<GroupExpenseGetResponseDTO> fetchExpensesByGroupId(String groupId, int limit , int offset)  {
        Page<ExpenseEntity> expenses = this.groupExpenseRepository.findExpensesListExceptPaymentsByGroupId(groupId, Pageable.ofSize(limit).withPage(offset));
        if (expenses == null || expenses.isEmpty()) {
            throw new NotFoundException("No expense found under group " + groupId);
        }
        PagedResults<GroupExpenseGetResponseDTO> groupExpenseGetResponseDTOPagedResults = PagedResults.<GroupExpenseGetResponseDTO>builder()
                                    .totalCount((int)expenses.getTotalElements())
                                    .pageCount(expenses.getTotalPages())
                                    .pageSize(limit)
                                    .results(expenses.getContent()
                                            .stream()
                                            .map(expense -> buildExpenseDTO(expense))
                                            .collect(Collectors.toList()))
                                    .build();
        return groupExpenseGetResponseDTOPagedResults;
    }

    @Override
    public GroupExpenseGetResponseDTO fetchExpenseByGroupIdAndExpenseId(String groupId, String expenseId) {
        ExpenseEntity expenseEntity = getExpenseByGroupIdAndExpenseId(groupId, expenseId);
        return buildExpenseDTO(expenseEntity);
    }

    @Override
    public GroupExpenseGetResponseDTO updateExpense(String groupId, String expenseId, GroupExpensePUTRequestDTO groupExpensePUTRequestDTO) {
        ExpenseEntity expenseEntity = getExpenseByGroupIdAndExpenseId(groupId, expenseId);
        expenseEntity = fromExpensePUTDTO(groupId, expenseEntity, groupExpensePUTRequestDTO);
        expenseEntity = groupExpenseRepository.save(expenseEntity);
        return buildExpenseDTO(expenseEntity);
    }

    @Override
    public void deleteExpense(String groupId, String expenseId) {
        ExpenseEntity expenseEntity = getExpenseByGroupIdAndExpenseId(groupId, expenseId);
        groupExpenseRepository.delete(expenseEntity);
    }

    private ExpenseEntity getExpenseByGroupIdAndExpenseId(String groupId, String expenseId) {
        Optional<ExpenseEntity> expenseEntityOpt = this.groupExpenseRepository.findByGroupIdAndExpenseId(groupId, expenseId);
        if (!expenseEntityOpt.isPresent()) {
            throw new NotFoundException(MessageFormat.format("Expense Id {0} not found under groupId {1}", groupId, expenseId));
        }
        return expenseEntityOpt.get();
    }

    private ExpenseEntity fromExpensePUTDTO(String groupId,ExpenseEntity expense , GroupExpensePUTRequestDTO groupExpensePUTRequestDTO) {
        if (groupExpensePUTRequestDTO.getExpenseName()!=null) {
            expense.setExpenseName(groupExpensePUTRequestDTO.getExpenseName());
        }
        if (groupExpensePUTRequestDTO.getTransactionDate()!=null) {
            expense.setExpenseDate(Date.valueOf(groupExpensePUTRequestDTO.getTransactionDate()));
        }
        if (groupExpensePUTRequestDTO.getCurrency()!=null) {
            expense.setCurrency(groupExpensePUTRequestDTO.getCurrency().name());
        }
        if (groupExpensePUTRequestDTO.getDescription()!=null) {
            expense.setDescription(groupExpensePUTRequestDTO.getDescription());
        }
        if (groupExpensePUTRequestDTO.getShareType()!=null) {
            expense.setExpenseType(ExpenseType.valueOf(groupExpensePUTRequestDTO.getShareType().name()));
         //   expense.getExpenseDetails().put("userShareType", groupExpensePUTRequestDTO.getShareType().name());
        }
        if (groupExpensePUTRequestDTO.getAmount()!=null) {
            expense.setAmount(groupExpensePUTRequestDTO.getAmount());
        }
        if (groupExpensePUTRequestDTO.getCurrency()!=null) {
            expense.setCurrency(groupExpensePUTRequestDTO.getCurrency().name());
        }
        if (groupExpensePUTRequestDTO.getPayee()!=null) {

            expense.getExpenseDetails().setPayee(groupExpensePUTRequestDTO.getPayee());
            List<String> payeeUsers = groupExpensePUTRequestDTO.getPayee().keySet().stream().collect(Collectors.toList());
            if (groupUsersRepository.countUsersUnderGroup(groupId,payeeUsers)  != payeeUsers.size()) {
                throw new BadRequestException("Payee users does not belong to requested group. Please re-verify payee users details.");
            }
        }
        if (groupExpensePUTRequestDTO.getShares()!=null) {
            expense.getExpenseDetails().setShares( groupExpensePUTRequestDTO.getShares().stream().map(Share::from).collect(Collectors.toList()));
            List<String> sharedUsers = groupExpensePUTRequestDTO.getShares().stream().map(share -> share.getUserId()).collect(Collectors.toList());
            if (groupUsersRepository.countUsersUnderGroup(groupId,sharedUsers)  != sharedUsers.size()) {
                throw new BadRequestException("Shared users does not belong to requested group. Please re-verify shared users details.");
            }
        }

        return expense;
    }
    private GroupExpenseGetResponseDTO buildExpenseDTO(ExpenseEntity expense) {
        return GroupExpenseGetResponseDTO.builder()
                .expenseId(expense.getExpenseId())
                .expenseName(expense.getExpenseName())
                .description(expense.getDescription())
                .transactionDate(expense.getExpenseDate().toString())
                .amount(expense.getAmount())
               // .shareType(expense.getExpenseType())
                .payee( expense.getExpenseDetails().getPayee())
                .shares(expense.getExpenseDetails().getShares().stream().map(val -> UserShare.from(val)).collect(Collectors.toList()))
                .build();
    }
}
