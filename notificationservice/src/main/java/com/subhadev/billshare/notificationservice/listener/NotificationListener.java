package com.subhadev.billshare.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subhadev.billshare.notificationservice.dto.NotificationEvent;
import com.subhadev.billshare.notificationservice.service.NotificationFacade;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {
    Logger logger = LoggerFactory.getLogger(NotificationListener.class);

    private NotificationFacade notificationFacade;

    ObjectMapper mapper = new ObjectMapper();

    public NotificationListener(NotificationFacade notificationFacade) {
        this.notificationFacade  = notificationFacade;
    }

    @KafkaListener(topics = "${app.kafka.notificationTopic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, String> payload) {
        logger.info("Event Received :: " + payload.value());

        try {
            NotificationEvent notificationEvent = mapper.readValue(payload.value(), NotificationEvent.class);
            notificationFacade.sendNotification(notificationEvent);
        } catch (Exception exception) {
            logger.error("Unable to send notification for following reason :: " + exception.getMessage());

        }
    }

}
