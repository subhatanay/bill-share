package com.subadev.billshare.groupbillshare.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="payment_transactions")
public class PaymentTransaction extends AuditEntity {
    @Id
    @GenericGenerator(name="uuid2",strategy = "uuid2")
    private String paymentId;

    private PaymentType paymentType;
    private Double amount;

    @ManyToOne
    @JoinColumn(name="payment_from",referencedColumnName = "userId")
    private UserEntity fromUser;

    @ManyToOne
    @JoinColumn(name="payment_to",referencedColumnName = "userId")
    private UserEntity toUser;

    @OneToOne
    @JoinColumn(name="expense_id",referencedColumnName = "expenseId")
    private ExpenseEntity expenseDetails;
}
