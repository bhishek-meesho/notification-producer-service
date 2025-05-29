package com.example.notificationservice.producer.controller;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notificationservice.producer.model.Notification;
import com.example.notificationservice.producer.service.KafkaNotificationProducerService;

@RestController         // this combines- @Controller and @ResponseBody
@RequestMapping("/api/notifications")
public class NotificationController{

    private KafkaNotificationProducerService producerService;

    public NotificationController(KafkaNotificationProducerService producerService){
        this.producerService = producerService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody Notification notification){
        if (notification.getType() == null || notification.getType().isEmpty() ||
        notification.getRecipient() == null || notification.getRecipient().isEmpty() ||
        notification.getMessage() == null || notification.getMessage().isEmpty()) {
        return new ResponseEntity<>("Notification 'type', 'recipient', and 'message' are required.", HttpStatus.BAD_REQUEST);
        }

         // Set timestamp if not provided by the client
         if (notification.getTimestamp() == null || notification.getTimestamp().isEmpty()) {
            notification.setTimestamp(Instant.now().toString());
        }

        producerService.sendNotification(notification);
        return new ResponseEntity<>("Notification request received and sent to Kafka.", HttpStatus.ACCEPTED);
    }

}
