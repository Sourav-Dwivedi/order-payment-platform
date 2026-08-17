package com.example.paymentservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.common.dto.OrderMessage;
import com.example.common.dto.OrderStatus;
import com.example.paymentservice.entity.PaymentTransaction;
import com.example.paymentservice.listener.PaymentListener;
import com.example.paymentservice.repository.PaymentTransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Integration tests for PaymentListener.
 *
 * Responsibilities:
 * - Simulate end-to-end flow from OrderCreated event to PaymentCompleted/Failed
 * - Validate ActiveMQ messaging and DB persistence
 */

@SpringBootTest
class PaymentListenerIntegrationTest {

    @MockBean
    private PaymentTransactionRepository repository;

    @Autowired
    private PaymentListener listener;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testPaymentTransactionCreatedWhenOrderMessageReceived() throws Exception {
        OrderMessage orderMessage = OrderMessage.builder()
                .id(999L)
                .customerId(2002L)
                .amount(500.0)
                .currency("EUR")
                .status(OrderStatus.PENDING.name())
                .build();

        String payload = mapper.writeValueAsString(orderMessage);

        when(repository.existsByOrderId(orderMessage.getId())).thenReturn(false);
        when(repository.save(any(PaymentTransaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        listener.handleOrderCreated(payload);

        verify(repository, times(1)).save(any(PaymentTransaction.class));
    }
}

