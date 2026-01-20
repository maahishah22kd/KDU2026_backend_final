package com.example.railwaybookingsystem.config;

import com.example.railwaybookingsystem.broker.InMemoryBroker;
import com.example.railwaybookingsystem.consumer.InventoryService;
import com.example.railwaybookingsystem.consumer.NotificationService;
import com.example.railwaybookingsystem.consumer.PaymentService;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class AppStartup {

    private final InMemoryBroker broker;
    private final InventoryService inventoryService;
    private final NotificationService notificationService;
    private final PaymentService paymentService;   // <-- added

    public AppStartup(InMemoryBroker broker,
                      InventoryService inventoryService,
                      NotificationService notificationService,
                      PaymentService paymentService) {
        this.broker = broker;
        this.inventoryService = inventoryService;
        this.notificationService = notificationService;
        this.paymentService = paymentService;
    }


    @PostConstruct
    public void init() {
        // create bindings (fan-out)
        broker.bind("ticket.booked", "inventory.ticket-booked.q");
        broker.bind("ticket.booked", "notification.ticket-booked.q");
        broker.bind("ticket.booked", "payment.ticket-booked.q"); // <-- add payment binding

        broker.registerHandler("inventory.ticket-booked.q", inventoryService);
        broker.registerHandler("notification.ticket-booked.q", notificationService);
        broker.registerHandler("payment.ticket-booked.q", paymentService);
    }
}
