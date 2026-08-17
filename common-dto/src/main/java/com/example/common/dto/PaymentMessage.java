package com.example.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMessage implements Serializable {
	private String eventId; // Unique event identifier
	private String eventType; // PAYMENT_COMPLETED or PAYMENT_FAILED
	private Long orderId; // Associated order
	private Long customerId; // Customer who placed the order
	private Double amount; // Payment amount
	private String currency; // Currency used
}
