package com.subhadev.billshare.notificationservice.dao;

import com.subhadev.billshare.notificationservice.entity.NotificationAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationAuditRepository extends JpaRepository<NotificationAuditEntity, String> {
}
