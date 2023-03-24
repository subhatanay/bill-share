package com.subadev.billshare.groupbillshare.entity;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="expenses")
public class ExpenseEntity extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @GenericGenerator(name="uuid2",strategy = "uuid2")
    private String expenseId;
    @ManyToOne
    @JoinColumn(name="group_id")
    private GroupEntity group;

    private Double amount;
    private String currency;
    private String expenseName;
    private String description;
    private ExpenseType expenseType;
    private Date expenseDate;

    @Column(name = "expense_details", columnDefinition = "text")
    @Convert(converter = HashMapConverter.class)
    private SettlementDetails expenseDetails;

}