package com.subhadev.billshare.userservice.service;

import com.subhadev.billshare.userservice.dto.NotificationEvent;
import com.subhadev.billshare.userservice.dto.NotificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Value("${app.kafka.notificationTopic}")
    private String notificationTopic;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public boolean sendNotificationEvent(String toAddress,NotificationType notificationType, Map<String, Object> attributes) {
        NotificationEvent event = NotificationEvent.builder()
                                    .notificationId(UUID.randomUUID().toString())
                                    .timestamp(new Date().getTime())
                                    .toAddress(toAddress)
                                    .notificationType(notificationType).attributes(attributes)
                                    .build();

        logger.info("Notification Event :: " + event);
        kafkaTemplate.send(notificationTopic,event);
        return true;
    }
}
