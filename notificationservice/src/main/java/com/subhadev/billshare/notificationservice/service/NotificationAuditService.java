package com.subhadev.billshare.notificationservice.service;

import com.subhadev.billshare.notificationservice.dto.NotificationEvent;

public interface NotificationAuditService {

    void createNotificationAudit(NotificationEvent notificationEvent, String status);



}
