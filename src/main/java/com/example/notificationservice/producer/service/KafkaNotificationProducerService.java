package com.example.notificationservice.producer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.notificationservice.producer.model.Notification;

@Service
public class KafkaNotificationProducerService {
   private final KafkaTemplate<String,Notification> kafkaTemplate;

   //insert topic names from configuration
    @Value("${app.kafka.topic.order-confirmation}")
    private String orderConfirmationTopic;
    @Value("${app.kafka.topic.shipping-update}")
    private String shippingUpdateTopic;
    @Value("${app.kafka.topic.promotional-offer}")
    private String promotionalOfferTopic;
    @Value("${app.kafka.topic.account-alert}")
    private String accountAlertTopic;
    @Value("${app.kafka.topic.payment-reminder}")
    private String paymentReminderTopic;

    
    public KafkaNotificationProducerService(KafkaTemplate<String,Notification> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(Notification notification){
        String topic = getTopicForNotificationType(notification.getType());

        if(topic==null) {
            System.err.println("Unknown notification type: "+ notification.getType() + ". Message not sent kafka.");
            return;
        }

        System.out.println("Sending notification to topic: " + topic + " | Notification: " + notification);

        kafkaTemplate.send(topic, notification.getRecipient(), notification)
        .whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent notification successfully to partition " + result.getRecordMetadata().partition() +
                        " with offset " + result.getRecordMetadata().offset());
            } else {
                System.err.println("Failed to send notification: " + ex.getMessage());
            }
        });
    }


    private String getTopicForNotificationType(String notificationType) {
        return switch (notificationType) {
            case "ORDER_CONFIRMATION" -> orderConfirmationTopic;
            case "SHIPPING_UPDATE" -> shippingUpdateTopic;
            case "PROMOTIONAL_OFFER" -> promotionalOfferTopic;
            case "ACCOUNT_ALERT" -> accountAlertTopic;
            case "PAYMENT_REMINDER" -> paymentReminderTopic;
            default -> null; // Unknown type
        };
    }

}
