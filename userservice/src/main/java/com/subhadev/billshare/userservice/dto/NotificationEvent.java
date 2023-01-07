package com.subhadev.billshare.userservice.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NotificationEvent implements Serializable {
    private String notificationId;
    private NotificationType notificationType;
    private long timestamp;
    private String toAddress;
    private Map<String, Object> attributes;

}
