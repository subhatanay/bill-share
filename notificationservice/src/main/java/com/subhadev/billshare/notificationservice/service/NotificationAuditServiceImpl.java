package com.subhadev.billshare.notificationservice.service;

import com.subhadev.billshare.notificationservice.dao.NotificationAuditRepository;
import com.subhadev.billshare.notificationservice.dto.NotificationEvent;
import com.subhadev.billshare.notificationservice.entity.NotificationAuditEntity;
import org.springframework.stereotype.Service;

@Service
public class NotificationAuditServiceImpl implements NotificationAuditService {
    NotificationAuditRepository notificationAuditRepository;

    public NotificationAuditServiceImpl(NotificationAuditRepository notificationAuditRepository) {
        this.notificationAuditRepository = notificationAuditRepository;
    }

    @Override
    public void createNotificationAudit(NotificationEvent notificationEvent, String status) {
        NotificationAuditEntity notificationAuditEntity = NotificationAuditEntity.builder()
                .notificationId(notificationEvent.getNotificationId())
                .notificationType(notificationEvent.getNotificationType().name())
                .toAddress(notificationEvent.getToAddress())
                .attributes(notificationEvent.getAttributes())
                .status(status)
                .build();

        notificationAuditRepository.save(notificationAuditEntity);
    }
}
