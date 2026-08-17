package com.example.orderservice.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.example.common.dto.OrderStatus;
import com.example.common.dto.PaymentMessage;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JMS listener for payment-related events.
 *
 * Responsibilities:
 * - Consume OrderCreated messages from ActiveMQ
 * - Process payment logic
 * - Publish PaymentCompleted or PaymentFailed events
 */

@Component
public class PaymentEventListener {

    private final OrderRepository repository;
    private final ObjectMapper mapper;

    public PaymentEventListener(OrderRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @JmsListener(destination = "PAYMENT_FAILED")
    public void handlePaymentFailed(String payload) throws Exception {
        PaymentMessage message = mapper.readValue(payload, PaymentMessage.class);
        Order order = repository.findById(message.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.setStatus(OrderStatus.PAYMENT_FAILED.name());
        repository.save(order);
    }

    @JmsListener(destination = "PAYMENT_COMPLETED")
    public void handlePaymentCompleted(String payload) throws Exception {
        PaymentMessage message = mapper.readValue(payload, PaymentMessage.class);
        Order order = repository.findById(message.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.setStatus(OrderStatus.PAID.name());
        repository.save(order);
    }
}
