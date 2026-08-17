package com.example.orderservice.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.common.dto.OrderMessage;
import com.example.common.dto.OrderStatus;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OutboxEvent;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

/**
 * Service layer for order management.
 *
 * Responsibilities:
 * - Create and cancel orders
 * - Save orders in DB with ACID transactions
 * - Publish OrderCreatedEvent to Outbox
 */

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository,
                        OutboxEventRepository outboxRepository,
                        ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * @param order
     * @return
     */
    @Transactional
    public Order createOrder(Order order) {
        // Step 1: Save order as PENDING
        order.setStatus(OrderStatus.PENDING.name());
        Order savedOrder = orderRepository.save(order);

        // Step 2: Update to PAYMENT_PROCESSING
        savedOrder.setStatus(OrderStatus.PAYMENT_PROCESSING.name());
        orderRepository.save(savedOrder);

        // Step 3: Publish ORDER_CREATED event using DTO
        try {
            OrderMessage message = OrderMessage.builder()
                .id(savedOrder.getId())
                .customerId(savedOrder.getCustomerId())
                .amount(savedOrder.getAmount())
                .currency(savedOrder.getCurrency())
                .build();

            OutboxEvent event = OutboxEvent.builder()
                .eventId("EVT-" + UUID.randomUUID())
                .eventType("ORDER_CREATED")
                .payload(objectMapper.writeValueAsString(message)) // ✅ DTO not entity
                .published(false)
                .build();

            outboxRepository.save(event);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return savedOrder;
    }

    /**
     * @param id
     * @return
     */
    @Transactional
    public Order cancelOrder(Long id) {
        Order order = getOrder(id);
        order.setStatus(OrderStatus.CANCELLED.name());
        Order saved = orderRepository.save(order);

        try {
            OrderMessage message = OrderMessage.builder()
                .id(saved.getId())
                .customerId(saved.getCustomerId())
                .amount(saved.getAmount())
                .currency(saved.getCurrency())
                .build();

            OutboxEvent event = OutboxEvent.builder()
                .eventId("EVT-" + UUID.randomUUID())
                .eventType("ORDER_CANCELLED")
                .payload(objectMapper.writeValueAsString(message)) // ✅ DTO not entity
                .published(false)
                .build();

            outboxRepository.save(event);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return saved;
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    public String getOrderStatus(Long id) {
        return getOrder(id).getStatus();
    }

    /**
     * @param id
     * @param status
     */
    @Transactional
    public void updateOrderStatus(Long id, String status) {
        Order order = getOrder(id);
        order.setStatus(status);
        orderRepository.save(order);
    }
}
