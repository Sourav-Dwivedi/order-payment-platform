package com.example.orderservice.event;

import java.util.List;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.orderservice.entity.OutboxEvent;
import com.example.orderservice.repository.OutboxEventRepository;

import jakarta.transaction.Transactional;

/**
 * Publishes events from the Outbox table to ActiveMQ.
 *
 * Responsibilities:
 * - Read pending events from OutboxEventRepository
 * - Send them to the broker
 * - Mark events as published
 */

@Component
public class OutboxPublisher {
    private final OutboxEventRepository outboxRepository;
    private final JmsTemplate jmsTemplate;

    public OutboxPublisher(OutboxEventRepository outboxRepository, JmsTemplate jmsTemplate) {
        this.outboxRepository = outboxRepository;
        this.jmsTemplate = jmsTemplate;
    }

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void publishEvents() {
        List<OutboxEvent> events = outboxRepository.findByPublishedFalse();
        for (OutboxEvent e : events) {
            try {
                jmsTemplate.convertAndSend(e.getEventType(), e.getPayload());
                e.setPublished(true);
                outboxRepository.save(e);
            } catch (Exception ex) {
                System.err.println("Failed to publish event " + e.getEventId() + ": " + ex.getMessage());
            }
        }
    }
}
