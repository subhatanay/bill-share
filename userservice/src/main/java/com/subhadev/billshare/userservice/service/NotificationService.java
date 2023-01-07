package com.subhadev.billshare.userservice.service;

import com.subhadev.billshare.userservice.dto.NotificationType;

import java.util.Map;

public interface NotificationService {

    boolean sendNotificationEvent(String toAddress,NotificationType notificationType, Map<String, Object> attributes);

}
