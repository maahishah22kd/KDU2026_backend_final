package com.example.railwaybookingsystem.consumer;

import com.example.railwaybookingsystem.broker.MessageEnvelope;
import com.example.railwaybookingsystem.broker.MessageHandler;
import com.example.railwaybookingsystem.model.TicketBookedMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Send confirmation SMS when Ticket_Booked arrives.
 * Here we simulate SMS by logging.
 */
@Slf4j
@Component
public class NotificationService implements MessageHandler {

    @Override
    public void handle(MessageEnvelope envelope) throws Exception {
        TicketBookedMessage msg = (TicketBookedMessage) envelope.getPayload();
        log.info("Notification received bookingId={} phone={}",
                msg.getBookingId(), msg.getPhone());

        log.info("SMS sent for bookingId={}", msg.getBookingId());
    }
}
