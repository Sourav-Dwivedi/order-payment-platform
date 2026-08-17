package com.example.orderservice.repository;

import com.example.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for Order entity.
 *
 * Responsibilities:
 * - CRUD operations on orders
 * - Query methods for order status
 */

public interface OrderRepository extends JpaRepository<Order, Long> {
}
