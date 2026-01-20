package com.example.railwaybookingsystem.consumer;

import com.example.railwaybookingsystem.broker.MessageEnvelope;
import com.example.railwaybookingsystem.broker.MessageHandler;
import com.example.railwaybookingsystem.model.TicketBookedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class PaymentService implements MessageHandler {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final Map<String, String> processedLedger = new ConcurrentHashMap<>();

    private final Set<String> processedTx = ConcurrentHashMap.newKeySet();

    @Override
    public void handle(MessageEnvelope envelope) throws Exception {
        TicketBookedMessage msg = (TicketBookedMessage) envelope.getPayload();

        String txId = "tx-" + msg.getBookingId();

        if (processedLedger.containsKey(txId)) {
            log.info("Transaction [{}] already processed (ledger). Ignoring duplicate.", txId);
            return;
        }

        if ("CRASH_BEFORE_ACK".equals(msg.getSeatNo()) && envelope.getDeliveryAttempts() == 0) {
            log.warn("Simulating crash before durable write for tx={} (deliveryAttempts={}). Throwing.", txId, envelope.getDeliveryAttempts());
            throw new RuntimeException("Simulated crash before durable write");
        }

        processedLedger.put(txId, Instant.now().toString());
        log.info("Durable ledger written for tx={} (booking={})", txId, msg.getBookingId());
        processedTx.add(txId);

        log.info("Money Deducted tx={} for booking={} amount={}", txId, msg.getBookingId(), /*amount*/ "SIMULATED");

    }
}
