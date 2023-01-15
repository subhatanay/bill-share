package com.subhadev.billshare.notificationservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotificationTemplateDTO {
    private String templateName;
    private String subject;
    private String senderType;
    private String templateContent;
}
