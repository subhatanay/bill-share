package com.subhadev.billshare.userservice.entity;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class AuditEntity {
    private Date createdTime;
    private Date lastUpdatedTime;
}
