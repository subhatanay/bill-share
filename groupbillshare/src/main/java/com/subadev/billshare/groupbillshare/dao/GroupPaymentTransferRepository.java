package com.subadev.billshare.groupbillshare.dao;

import com.subadev.billshare.groupbillshare.entity.PaymentTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupPaymentTransferRepository extends JpaRepository<PaymentTransaction, String> {


    @Query(value = "SELECT pay from payment_transactions pay WHERE pay.groupEntity.groupId=?1 and pay.paymentId=?2")
    Optional<PaymentTransaction> findByGroupIdAndPaymentId(String groupId, String paymentId);

    @Query(value = "SELECT pay from payment_transactions pay WHERE pay.groupEntity.groupId=?1",
            countQuery = "SELECT pay from payment_transactions pay WHERE pay.groupEntity.groupId=?1")
    Page<PaymentTransaction> findByGroupId(String groupId, Pageable page);

}
