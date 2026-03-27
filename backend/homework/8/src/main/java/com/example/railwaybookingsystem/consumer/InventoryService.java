package com.example.railwaybookingsystem.consumer;

import com.example.railwaybookingsystem.broker.MessageEnvelope;
import com.example.railwaybookingsystem.broker.MessageHandler;
import com.example.railwaybookingsystem.model.TicketBookedMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 Mark seat occupied when Ticket_Booked arrives.
 */
@Slf4j
@Component
public class InventoryService implements MessageHandler {

    @Override
    public void handle(MessageEnvelope envelope) throws Exception {
        TicketBookedMessage msg = (TicketBookedMessage) envelope.getPayload();

        log.info("Inventory received bookingId={} train={} seat={}",
                msg.getBookingId(), msg.getTrainId(), msg.getSeatNo());

        if (msg.getAge() < 0) {
            log.warn("Inventory detected invalid message (age<0) bookingId={}. Throwing to trigger retry.", msg.getBookingId());
            throw new IllegalArgumentException("Invalid age: " + msg.getAge());
        }

        log.info("Seat marked occupied for bookingId={}", msg.getBookingId());
    }
}
