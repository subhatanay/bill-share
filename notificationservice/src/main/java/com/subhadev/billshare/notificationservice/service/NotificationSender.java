package com.subhadev.billshare.notificationservice.service;

public interface NotificationSender {

    boolean send(String toAddress , String subject, String message);
}
