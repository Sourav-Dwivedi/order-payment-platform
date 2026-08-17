package com.example.paymentservice.listener;

import com.example.common.dto.OrderMessage;
import com.example.common.dto.OrderStatus;
import com.example.paymentservice.entity.PaymentTransaction;
import com.example.paymentservice.repository.PaymentTransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * JMS listener for order-related events.
 *
 * Responsibilities:
 * - Consume OrderCreated messages from ActiveMQ
 * - Trigger payment processing logic
 * - Ensure idempotency by checking existing transactions
 * - Publish PaymentCompleted or PaymentFailed events
 */

@Component
public class PaymentListener {

    private final PaymentTransactionRepository repository;
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper mapper;
    private final List<String> supportedCurrencies;

    public PaymentListener(PaymentTransactionRepository repository,
                           JmsTemplate jmsTemplate,
                           ObjectMapper mapper,
                           @Value("${payment.supported.currencies}") String currencies) {
        this.repository = repository;
        this.jmsTemplate = jmsTemplate;
        this.mapper = mapper;
        this.supportedCurrencies = Arrays.asList(currencies.split(","));
    }

    @JmsListener(destination = "ORDER_CREATED", containerFactory = "jmsListenerContainerFactory")
    public void handleOrderCreated(String payload) throws Exception {
        OrderMessage order = mapper.readValue(payload, OrderMessage.class);

        // Idempotency check
        if (repository.existsByOrderId(order.getId())) {
            return;
        }

        try {
            boolean validAmount = order.getAmount() != null && order.getAmount() > 0;
            boolean validCurrency = order.getCurrency() != null &&
                                    supportedCurrencies.contains(order.getCurrency());

            boolean success = validAmount && validCurrency;

            // Persist transaction
            PaymentTransaction tx = PaymentTransaction.builder()
                    .orderId(order.getId())
                    .amount(order.getAmount())
                    .currency(order.getCurrency())
                    .status(success ? OrderStatus.PAID.name() : OrderStatus.PAYMENT_FAILED.name())
                    .build();
            repository.save(tx);

            // Publish result event
            String eventType = success ? OrderStatus.PAYMENT_COMPLETED.name() : OrderStatus.PAYMENT_FAILED.name();
            Map<String, Object> eventPayload = Map.of(
                    "eventId", "EVT-" + UUID.randomUUID(),
                    "eventType", eventType,
                    "orderId", order.getId(),
                    "customerId", order.getCustomerId(),
                    "amount", order.getAmount(),
                    "currency", order.getCurrency()
            );

            jmsTemplate.convertAndSend(eventType, mapper.writeValueAsString(eventPayload));

        } catch (Exception ex) {
            // Let ActiveMQ retry transient failures
            throw new JMSException("Transient failure: " + ex.getMessage());
        }
    }
}
