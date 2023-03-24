package com.subhadev.billshare.notificationservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Configuration
public class JavaEmailConfig {
    @Value("${app.notification.email.username}")
    private String emailUsername;

    @Value("${app.notification.email.password}")
    private String emailPassword;
    public Properties getEmailProperties() {
        Properties props = new Properties() {{
            put("mail.smtp.auth", "true");
            put("mail.smtp.host", "smtp.gmail.com");
            put("mail.transport.protocol", "smtp");
            put("mail.smtp.port", "587");
            put("mail.smtp.starttls.enable", "true");
            put("mail.smtp.ssl.protocols", "TLSv1.2");
            put("mail.user", emailUsername);
            put("mail.password", emailPassword);
        }};

        return props;
    }
    public Session getSessionInstance() {
        Session session = Session.getInstance(getEmailProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUsername, emailPassword);
            }
        });
        return session;
    }
}
