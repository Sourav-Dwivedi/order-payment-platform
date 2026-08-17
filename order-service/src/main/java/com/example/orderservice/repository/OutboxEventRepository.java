package com.example.orderservice.repository;

import com.example.orderservice.entity.OutboxEvent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for OutboxEvent entity.
 *
 * Responsibilities:
 * - CRUD operations on outbox events
 * - Fetch unpublished events for publishing
 */

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

	List<OutboxEvent> findByPublishedFalse();
}
