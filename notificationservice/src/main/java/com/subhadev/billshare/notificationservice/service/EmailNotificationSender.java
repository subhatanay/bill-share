package com.subhadev.billshare.notificationservice.service;

import com.subhadev.billshare.notificationservice.config.JavaEmailConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailNotificationSender implements  NotificationSender {
    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationSender.class);

    @Value("${app.notification.email.fromAddress:noreply@billshare.com}")
    private String fromAddress;

    private JavaEmailConfig javaEmailConfig;

    public EmailNotificationSender(JavaEmailConfig javaEmailConfig) {
        this.javaEmailConfig = javaEmailConfig;
    }

    @Override
    public boolean send(String toAddress, String subject, String body) {
        if (toAddress == null || subject == null || body == null) {
            throw new IllegalArgumentException("toAddress or subject or message should not be null");
        }
        try {
            Session session = javaEmailConfig.getSessionInstance();

            Message message = new MimeMessage(session);
            message.setSentDate(new Date());
            message.setFrom(new InternetAddress(fromAddress));
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(toAddress));
            message.setSubject(subject);
            message.setText(body);

            // Send a message
            Transport.send(message);
            return true;
        } catch (Exception exception) {
            logger.error("Unable to send email. Reason :: " + exception.getMessage());
            return false;
        }
    }
}
