package com.example.paymentservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.dto.OrderStatus;
import com.example.paymentservice.entity.PaymentTransaction;
import com.example.paymentservice.repository.PaymentTransactionRepository;

/**
 * REST controller for managing payment operations.
 *
 * Endpoints:
 * - Process a payment request
 * - Fetch payment transaction details
 *
 * Responsibilities:
 * - Handle HTTP requests related to payments
 * - Delegate business logic to PaymentService or PaymentListener
 */

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentTransactionRepository repository;

    public PaymentController(PaymentTransactionRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<PaymentTransaction> createPayment(@RequestBody PaymentTransaction tx) {
        tx.setStatus(OrderStatus.PAID.name());
        PaymentTransaction saved = repository.save(tx);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentTransaction> getPayment(@PathVariable Long orderId) {
        return repository.findByOrderId(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
