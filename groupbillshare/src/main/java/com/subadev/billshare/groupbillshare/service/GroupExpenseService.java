package com.subadev.billshare.groupbillshare.service;

import com.subadev.billshare.groupbillshare.dto.*;

public interface GroupExpenseService {

    GroupExpenseResponseDTO createExpense(String groupId, GroupExpenseRequestDTO groupExpenseRequestDTO);

    PagedResults<GroupExpenseGetResponseDTO> fetchExpensesByGroupId(String groupId, int limit , int offset);

    GroupExpenseGetResponseDTO fetchExpenseByGroupIdAndExpenseId(String groupId, String expenseId);

    GroupExpenseGetResponseDTO updateExpense(String groupId, String expenseId, GroupExpensePUTRequestDTO groupExpensePUTRequestDTO);

    void deleteExpense(String groupId, String expenseId);
}
