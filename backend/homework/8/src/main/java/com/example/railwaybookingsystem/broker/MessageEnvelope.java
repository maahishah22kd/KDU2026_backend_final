package com.example.railwaybookingsystem.broker;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageEnvelope {
    private Object payload;
    private String routingKey;
    private int deliveryAttempts = 0;
    private String id;
}
