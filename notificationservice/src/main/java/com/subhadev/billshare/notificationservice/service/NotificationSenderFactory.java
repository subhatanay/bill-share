package com.subhadev.billshare.notificationservice.service;

import org.springframework.stereotype.Component;

@Component
public class NotificationSenderFactory {

    private NotificationSender emailNotificationService;

    public NotificationSenderFactory(NotificationSender emailNotificationService) {
        this.emailNotificationService = emailNotificationService;
    }

    public NotificationSender getSenderInstance(String senderType) {
        switch(senderType) {
            case "EMAIL" :
                return emailNotificationService;
            default:
                return null;
        }
    }

}
