package com.subhadev.billshare.notificationservice.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Map;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "notification_audit")
public class NotificationAuditEntity {

    @Id
    private String notificationId;

    private String notificationType;

    private String toAddress;

    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> attributes;

    private String status;


}
