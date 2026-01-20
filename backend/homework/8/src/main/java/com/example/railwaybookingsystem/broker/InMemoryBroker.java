package com.example.railwaybookingsystem.broker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.*;


@Slf4j
@Component
public class InMemoryBroker {

    private final Map<String, List<String>> topicToQueues = new ConcurrentHashMap<>();
    private final Map<String, BlockingQueue<MessageEnvelope>> queues = new ConcurrentHashMap<>();
    private final Map<String, MessageHandler> handlers = new ConcurrentHashMap<>();
    private final ExecutorService workers = Executors.newCachedThreadPool();

    private final int maxAttempts = 3;
    private final long baseBackoffMillis = 1000L;

    public static final String BOOKING_DLQ = "booking-error-queue";

    private final Set<String> startedWorkers = ConcurrentHashMap.newKeySet();

    public InMemoryBroker() {
        queues.putIfAbsent(BOOKING_DLQ, new LinkedBlockingQueue<>());
    }

    public void createQueueIfAbsent(String queueName) {
        queues.computeIfAbsent(queueName, q -> new LinkedBlockingQueue<>());
    }

    /** Bind a routing key to a queue*/
    public void bind(String routingKey, String queueName) {
        topicToQueues.computeIfAbsent(routingKey, k -> new CopyOnWriteArrayList<>()).add(queueName);
        createQueueIfAbsent(queueName);
    }

    /** Register a handler for a queue and start its worker */
    public void registerHandler(String queueName, MessageHandler handler) {
        handlers.put(queueName, handler);
        createQueueIfAbsent(queueName);
        startWorkerIfNeeded(queueName);
    }

    private void startWorkerIfNeeded(String queueName) {
        if (!startedWorkers.add(queueName)) return;
        workers.submit(() -> {
            BlockingQueue<MessageEnvelope> q = queues.get(queueName);
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    MessageEnvelope envelope = q.take(); // blocking
                    processEnvelope(queueName, envelope);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        });
    }

    /** Publish to a routing key */
    public void publish(String routingKey, MessageEnvelope envelope) {
        List<String> qnames = topicToQueues.getOrDefault(routingKey, Collections.emptyList());
        for (String q : qnames) {
            MessageEnvelope copy = new MessageEnvelope(
                    envelope.getPayload(),
                    envelope.getRoutingKey(),
                    0,
                    envelope.getId()
            );
            BlockingQueue<MessageEnvelope> queue = queues.get(q);
            if (queue != null) {
                queue.offer(copy);
                log.info("Enqueued message id={} to queue={}", copy.getId(), q);
            }
        }
    }

    /** Direct push into specific queue (for admin/test) */
    public void pushToQueue(String queue, MessageEnvelope envelope) {
        createQueueIfAbsent(queue);
        queues.get(queue).offer(envelope);
        log.info("Directly pushed message id={} to queue={}", envelope.getId(), queue);
    }

    /** Process envelope for queue with retry and DLQ */
    private void processEnvelope(String queueName, MessageEnvelope envelope) {
        MessageHandler handler = handlers.get(queueName);
        if (handler == null) {
            // no handler yet -> requeue after a short wait
            try { TimeUnit.MILLISECONDS.sleep(200); } catch (InterruptedException ignored) {}
            queues.get(queueName).offer(envelope);
            return;
        }

        try {
            handler.handle(envelope); // may throw
            // success -> consumed
        } catch (Exception e) {
            envelope.setDeliveryAttempts(envelope.getDeliveryAttempts() + 1);
            if (envelope.getDeliveryAttempts() >= maxAttempts) {
                log.error(
                        "Message id={} moved to DLQ after {} attempts. Reason={}",
                        envelope.getId(),
                        envelope.getDeliveryAttempts(),
                        e.getMessage()
                );
                queues.get(BOOKING_DLQ).offer(envelope);
            } else {
                long backoff = baseBackoffMillis * envelope.getDeliveryAttempts();
                log.warn(
                        "Handler failed for message id={} on queue={} attempt={} retrying after {}ms",
                        envelope.getId(),
                        queueName,
                        envelope.getDeliveryAttempts(),
                        backoff
                );

                workers.submit(() -> {
                    try { TimeUnit.MILLISECONDS.sleep(backoff); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
                    queues.get(queueName).offer(envelope);
                });
            }
        }
    }

    /** Drain DLQ for inspection (returns and removes) */
    public List<MessageEnvelope> drainDlq() {
        List<MessageEnvelope> out = new ArrayList<>();
        BlockingQueue<MessageEnvelope> dlq = queues.get(BOOKING_DLQ);
        if (dlq != null) dlq.drainTo(out);
        return out;
    }

    @PreDestroy
    public void shutdown() {
        workers.shutdownNow();
    }
}
