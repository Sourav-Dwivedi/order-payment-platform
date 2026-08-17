package com.example.paymentservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.paymentservice.entity.PaymentTransaction;

/**
 * JPA repository for PaymentTransaction entity.
 *
 * Responsibilities:
 * - CRUD operations on payment transactions
 * - Query methods for finding transactions by orderId
 * - Ensure idempotency by checking if a transaction already exists
 */

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
	
	// Custom finder method
    Optional<PaymentTransaction> findByOrderId(Long orderId);

    // For idempotency check
    boolean existsByOrderId(Long orderId);
}
