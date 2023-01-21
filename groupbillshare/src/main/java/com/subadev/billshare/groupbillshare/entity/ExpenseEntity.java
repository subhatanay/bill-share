package com.subadev.billshare.groupbillshare.entity;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="expenses")
public class ExpenseEntity extends AuditEntity {
    @Id
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

    @Column(name = "expense_details", length = 2000)
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> expenseDetails;

}