package com.example.paymentservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.common.dto.OrderStatus;
import com.example.paymentservice.entity.PaymentTransaction;
import com.example.paymentservice.repository.PaymentTransactionRepository;

/**
 * Unit tests for PaymentTransactionRepository.
 *
 * Responsibilities:
 * - Verify CRUD operations on payment transactions
 * - Ensure queries return expected results
 */

@SpringBootTest
class PaymentTransactionRepositoryTest {

    @Autowired
    private PaymentTransactionRepository repository;

    @BeforeEach
    void cleanDb() {
        repository.deleteAll();
    }

    @Test
    void testSaveAndFindPaymentTransaction() {
        PaymentTransaction tx = PaymentTransaction.builder()
                .orderId(101L)
                .amount(200.0)
                .currency("USD")
                .status(OrderStatus.PAID.name())
                .build();

        repository.save(tx);

        assertThat(repository.findByOrderId(101L)).isPresent();
    }

    @Test
    void testDuplicateOrderIdThrowsException() {
        PaymentTransaction tx1 = PaymentTransaction.builder()
                .orderId(101L)
                .amount(200.0)
                .currency("USD")
                .status(OrderStatus.PAID.name())
                .build();
        repository.save(tx1);

        PaymentTransaction tx2 = PaymentTransaction.builder()
                .orderId(101L)
                .amount(300.0)
                .currency("USD")
                .status(OrderStatus.PAYMENT_FAILED.name())
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> repository.save(tx2));
    }
}

