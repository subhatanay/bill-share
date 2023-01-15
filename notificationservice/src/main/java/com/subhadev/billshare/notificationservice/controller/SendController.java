package com.subhadev.billshare.notificationservice.controller;

import com.subhadev.billshare.notificationservice.dto.NotificationEvent;
import com.subhadev.billshare.notificationservice.service.NotificationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send")
public class SendController {

    NotificationFacade notificationFacade;

    public SendController(NotificationFacade notificationFacade) {
        this.notificationFacade = notificationFacade;
    }


    @PostMapping()
    public ResponseEntity sendMessage(@RequestBody NotificationEvent event) {
        notificationFacade.sendNotification(event);
        return ResponseEntity.ok().build();
    }
}
