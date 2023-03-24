package com.subadev.billshare.groupbillshare.dao;

import com.subadev.billshare.groupbillshare.entity.ExpenseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupExpenseRepository extends JpaRepository<ExpenseEntity, String> {

    @Query(value = "SELECT ex from expenses ex JOIN groups g  ON ex.group.groupId=g.groupId WHERE g.groupId=?1",
            countQuery = "SELECT count(ex) from expenses ex JOIN groups g ON ex.group.groupId=g.groupId WHERE g.groupId=?1")
    Page<ExpenseEntity> findByGroupId(String groupId, Pageable pageable);

    @Query(value = "SELECT ex from expenses ex JOIN groups g  ON ex.group.groupId=g.groupId LEFT JOIN payment_transactions pay ON NOT pay.expenseDetails.expenseId=ex.expenseId WHERE g.groupId=?1",
            countQuery = "SELECT count(ex) from expenses ex JOIN groups g  ON ex.group.groupId=g.groupId JOIN payment_transactions pay ON NOT pay.expenseDetails.expenseId=ex.expenseId WHERE g.groupId=?1")
    Page<ExpenseEntity> findExpensesListExceptPaymentsByGroupId(String groupId, Pageable pageable);

    @Query(value = "SELECT ex from expenses ex JOIN groups g ON ex.group.groupId=g.groupId WHERE g.groupId=?1 and ex.expenseId=?2")
    Optional<ExpenseEntity> findByGroupIdAndExpenseId(String groupId, String expenseId);



}
