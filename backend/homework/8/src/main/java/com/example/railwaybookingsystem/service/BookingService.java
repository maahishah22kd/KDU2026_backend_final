package com.example.railwaybookingsystem.service;

import com.example.railwaybookingsystem.broker.InMemoryBroker;
import com.example.railwaybookingsystem.broker.MessageEnvelope;
import com.example.railwaybookingsystem.model.BookingRequest;
import com.example.railwaybookingsystem.model.BookingResponse;
import com.example.railwaybookingsystem.model.TicketBookedMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@AllArgsConstructor
@Service
public class BookingService{
    private final Map<String, BookingRequest> bookings = new ConcurrentHashMap<>();
    private final InMemoryBroker broker;

    public BookingResponse createBookingAndPublish(BookingRequest req){
        String bookingId = "bkg-" + UUID.randomUUID();
        bookings.put(bookingId, req);

        TicketBookedMessage msg = new TicketBookedMessage(
                UUID.randomUUID().toString(), // eventId
                bookingId,
                req.getTrainId(),
                req.getSeatNo(),
                req.getPhone(),
                req.getAge()
        );

        MessageEnvelope env = new MessageEnvelope(msg, "ticket.booked", 0, msg.getEventId());
        broker.publish("ticket.booked", env);

        log.info("Booking created. bookingId={} eventId={}",
                bookingId, msg.getEventId());
        return new BookingResponse(bookingId, "BOOKING_IN_PROGRESS");
    }

}