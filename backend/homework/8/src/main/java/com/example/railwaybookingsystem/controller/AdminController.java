package com.example.railwaybookingsystem.controller;

import com.example.railwaybookingsystem.broker.InMemoryBroker;
import com.example.railwaybookingsystem.broker.MessageEnvelope;
import com.example.railwaybookingsystem.model.TicketBookedMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final InMemoryBroker broker;

    public AdminController(InMemoryBroker broker) {
        this.broker = broker;
    }

    @GetMapping("/dlq/drain")
    public List<MessageEnvelope> drainDlq() {
        return broker.drainDlq();
    }

    @PostMapping("/push-payment")
    public String pushPayment(@RequestBody TicketBookedMessage msg,
                              @RequestParam(defaultValue = "1") int times) {
        MessageEnvelope env = new MessageEnvelope(msg, "ticket.booked", 0, msg.getEventId());
        for (int i = 0; i < times; i++) {
            broker.pushToQueue("payment.ticket-booked.q", env);
            log.info("Admin pushed payment message id={} to payment.queue (#{})", env.getId(), i+1);
        }
        return "pushed payment msg " + times + " times";
    }

}
