package com.subhadev.billshare.notificationservice.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "notification_templates")
public class NotificationTemplateEntity extends AuditEntity {
    @Id
    @GenericGenerator(name="uuid2",strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    private String templateId;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "template_subject", length = 255)
    private String templateSubject;

    @Column(name = "template_content", length = 2000)
    private String templateContent;


    @Column(name = "sender_type", length = 2000)
    private SenderType senderType;

    public static enum SenderType {
        EMAIL
    }



}
