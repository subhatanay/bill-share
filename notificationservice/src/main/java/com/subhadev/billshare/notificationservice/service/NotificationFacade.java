package com.subhadev.billshare.notificationservice.service;

import com.subhadev.billshare.notificationservice.dto.NotificationEvent;
import com.subhadev.billshare.notificationservice.dto.NotificationTemplateDTO;
import com.subhadev.billshare.notificationservice.helper.TemplateParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationFacade {

    private static final Logger logger = LoggerFactory.getLogger(NotificationFacade.class);

    private  NotificationSenderFactory notificationSenderFactory;
    private  NotificationTemplateService notificationTemplateService;

    private NotificationAuditService notificationAuditService;

    public NotificationFacade(NotificationSenderFactory notificationSenderFactory, NotificationTemplateService notificationTemplateService,NotificationAuditService notificationAuditService) {
        this.notificationSenderFactory = notificationSenderFactory;
        this.notificationTemplateService = notificationTemplateService;
        this.notificationAuditService = notificationAuditService;
    }

    public void sendNotification(NotificationEvent notificationEvent) {
        List<String> notificationTemplateTypes = notificationEvent.getNotificationType().getTypes();

        notificationTemplateTypes.forEach(template -> {
            NotificationTemplateDTO notificationTemplateDTO = notificationTemplateService.findNotificationTemplateByTemplateName(template);
            if (notificationEvent.getAttributes() !=null) {
                notificationTemplateDTO.setTemplateContent(TemplateParser.parseTemplate(notificationTemplateDTO.getTemplateContent(), notificationEvent.getAttributes()));
            }
            NotificationSender notificationSender = notificationSenderFactory.getSenderInstance(notificationTemplateDTO.getSenderType());
            if (notificationSender !=null) {
                boolean isSuccess = notificationSender.send(notificationEvent.getToAddress(),notificationTemplateDTO.getSubject(), notificationTemplateDTO.getTemplateContent());
                if (isSuccess) {
                    this.notificationAuditService.createNotificationAudit(notificationEvent, "SENT");
                } else {
                    this.notificationAuditService.createNotificationAudit(notificationEvent, "NOT SENT");
                }
            } else {
                logger.info(notificationTemplateDTO.getSenderType() + " Notification sender instance is turned off. Please check config and turn on");
            }
        });
    }


}
