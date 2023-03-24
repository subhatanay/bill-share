package com.subadev.billshare.groupbillshare.service;

import com.subadev.billshare.groupbillshare.dao.GroupExpenseRepository;
import com.subadev.billshare.groupbillshare.dao.GroupRepository;
import com.subadev.billshare.groupbillshare.dto.GroupExpenseSettlementResponseDTO;
import com.subadev.billshare.groupbillshare.entity.ExpenseEntity;
import com.subadev.billshare.groupbillshare.entity.GroupEntity;
import com.subadev.billshare.groupbillshare.exception.BadRequestException;
import com.subadev.billshare.groupbillshare.exception.NotFoundException;
import com.subadev.billshare.groupbillshare.service.settlement.ExpenseSettlementFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpenseSettlementServiceImpl implements ExpenseSettlementService {
    private static Logger logger = LoggerFactory.getLogger(ExpenseSettlementServiceImpl.class);
    GroupRepository groupRepository;
    GroupExpenseRepository groupExpenseRepository;
    ExpenseSettlementFactory expenseSettlementFactory;

    public ExpenseSettlementServiceImpl(GroupRepository groupRepository,
                                        GroupExpenseRepository groupExpenseRepository,
                                        ExpenseSettlementFactory expenseSettlementFactory) {
        this.groupRepository = groupRepository;
        this.groupExpenseRepository = groupExpenseRepository;
        this.expenseSettlementFactory = expenseSettlementFactory;
    }

    @Override
    public GroupExpenseSettlementResponseDTO getSettlementInfoByGroupIdAndCurrentUserId(String groupId, String currentUserId) {
        Optional<GroupEntity> groupEntityOptional = groupRepository.findById(groupId);
        if (!groupEntityOptional.isPresent()) {
            throw new NotFoundException("Group with Id " + groupId + " not exists.");
        }
        if (!groupRepository.isUserPresentBelongToGroup(groupId, currentUserId)) {
            throw new BadRequestException(MessageFormat.format("User {} does not belong to group {id}" , groupId, currentUserId));
        }

        Page<ExpenseEntity> expenseList = groupExpenseRepository.findByGroupId(groupId, Pageable.unpaged());
        if (expenseList.getTotalElements() <= 0) {
            throw new NotFoundException("No expense found for settlement for group with id " + groupId);
        }
        Map<String, Map<String,Double>> groupWiseSettlementDetails = expenseSettlementFactory.getExpenseSettlement().getSettlementDetails(expenseList.getContent());

        logger.info("" + groupWiseSettlementDetails);

        final List<GroupExpenseSettlementResponseDTO.UserSettlementInfo> userSettlementInfoList = new ArrayList<>();
        if (groupWiseSettlementDetails.containsKey(currentUserId)) {
            userSettlementInfoList.addAll(groupWiseSettlementDetails.get(currentUserId).entrySet().stream()
                    .map(set -> GroupExpenseSettlementResponseDTO.UserSettlementInfo
                                            .builder().userId(set.getKey()).amount(set.getValue()).build())
                    .collect(Collectors.toList()));
        }

        groupWiseSettlementDetails.entrySet().stream().forEach(payeeInfoMap -> {
            String payeeUserId = payeeInfoMap.getKey();
            logger.info("-- " + payeeUserId);
            logger.info("-- " + currentUserId);
            if (payeeInfoMap.getValue().containsKey(currentUserId)) {
                logger.info("--> " + currentUserId + " " + payeeInfoMap.getValue().get(currentUserId));
                userSettlementInfoList.add(GroupExpenseSettlementResponseDTO.UserSettlementInfo
                        .builder().userId(payeeUserId).amount(payeeInfoMap.getValue().get(currentUserId) * -1)
                        .build());
            }
        });
        return GroupExpenseSettlementResponseDTO.builder().settlementList(userSettlementInfoList).build();
    }
}
