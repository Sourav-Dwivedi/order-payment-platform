package com.example.orderservice.event;

import com.example.orderservice.entity.Order;

/**
 * Domain event representing that an order has been created.
 *
 * Responsibilities:
 * - Encapsulate order details
 * - Published by OrderService
 * - Consumed by PaymentService
 */

public class OrderCreatedEvent {
    private final Order order;

    public OrderCreatedEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
