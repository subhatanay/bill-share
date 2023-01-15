package com.subhadev.billshare.notificationservice.service;

import com.subhadev.billshare.notificationservice.dto.NotificationTemplateDTO;

public interface NotificationTemplateService {

    NotificationTemplateDTO findNotificationTemplateByTemplateName(String template);



}
