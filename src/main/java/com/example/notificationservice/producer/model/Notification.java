package com.example.notificationservice.producer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private String type;        // e.g., "ORDER_CONFIRMATION", "SHIPPING_UPDATE", "PROMOTIONAL_OFFER"
    private String recipient;   // Email address, phone number, or user ID
    private String subject;     // Subject line for email, or short intro for other types
    private String message;     // The main content of the notification
    private String timestamp;   // When the notification was created (ISO 8601 format: YYYY-MM-DDThh:mm:ssZ)
    private String orderId;     // Optional: For e-commerce context, link to an order
    private String userId;      // Optional: For user-specific context
}
