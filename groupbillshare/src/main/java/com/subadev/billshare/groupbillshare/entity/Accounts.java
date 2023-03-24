package com.subadev.billshare.groupbillshare.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="accounts")
public class Accounts {
    @Id
    private Integer id;

    private boolean processed;
}
