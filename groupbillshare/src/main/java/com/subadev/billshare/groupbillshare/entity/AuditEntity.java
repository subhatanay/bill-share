package com.subadev.billshare.groupbillshare.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@Setter
@Getter
public class AuditEntity {
    private Date createdTime;
    private Date lastUpdatedTime;
}
