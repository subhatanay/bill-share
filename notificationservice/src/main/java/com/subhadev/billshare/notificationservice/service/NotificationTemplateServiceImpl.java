package com.subhadev.billshare.notificationservice.service;

import com.subhadev.billshare.notificationservice.dao.NotificationTemplateRepository;
import com.subhadev.billshare.notificationservice.dto.NotificationTemplateDTO;
import com.subhadev.billshare.notificationservice.entity.NotificationTemplateEntity;
import com.subhadev.billshare.notificationservice.exception.TemplateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationTemplateServiceImpl implements NotificationTemplateService {
    private static Logger logger = LoggerFactory.getLogger(NotificationTemplateServiceImpl.class);

    private  NotificationTemplateRepository notificationTemplateRepository;

    public NotificationTemplateServiceImpl(NotificationTemplateRepository notificationTemplateRepository) {
        this.notificationTemplateRepository = notificationTemplateRepository;
    }

    @Override
    public NotificationTemplateDTO findNotificationTemplateByTemplateName(String template) {
        logger.info("---> " + template + " " );
        Optional<NotificationTemplateEntity> notificationTemplateEntityOpt = notificationTemplateRepository.findByTemplateName(template);
        if (!notificationTemplateEntityOpt.isPresent()) {
            throw new TemplateNotFoundException("Notification template not found");
        }
        NotificationTemplateEntity notificationTemplateEntity=  notificationTemplateEntityOpt.get();

        return NotificationTemplateDTO.builder()
                .templateName(notificationTemplateEntity.getTemplateName())
                .subject(notificationTemplateEntity.getTemplateSubject())
                .templateContent(notificationTemplateEntity.getTemplateContent())
                .senderType(notificationTemplateEntity.getSenderType().name()).build();

    }
}
