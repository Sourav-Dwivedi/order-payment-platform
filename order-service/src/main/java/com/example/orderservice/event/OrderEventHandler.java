package com.example.orderservice.event;

import java.util.UUID;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.common.dto.OrderMessage;
import com.example.orderservice.entity.Order;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handles domain events related to orders.
 *
 * Responsibilities:
 * - Listen for OrderCreatedEvent
 * - Trigger business workflows
 */

@Component
public class OrderEventHandler {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public OrderEventHandler(JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    @TransactionalEventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        try {
            Order order = event.getOrder();

            // Build the common DTO
            OrderMessage message = OrderMessage.builder()
                    .id(order.getId())
                    .customerId(order.getCustomerId())
                    .amount(order.getAmount())
                    .currency(order.getCurrency())
                    .status(order.getStatus())
                    .build();

            // Wrap with event metadata
            var payload = objectMapper.writeValueAsString(message);

            jmsTemplate.convertAndSend("ORDER_CREATED", payload);

            System.out.println("Published ORDER_CREATED event for orderId=" + order.getId()
                    + " with eventId=" + UUID.randomUUID());

        } catch (Exception ex) {
            System.err.println("Failed to publish ORDER_CREATED event: " + ex.getMessage());
        }
    }
}

