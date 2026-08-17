package com.example.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

/**
 * Entry point for the Payment Service Spring Boot application.
 *
 * Responsibilities:
 * - Bootstraps the Spring context
 * - Starts embedded Tomcat server
 * - Initializes beans and configurations for payment processing
 */

@SpringBootApplication
@EnableJms
public class PaymentServiceApplication {
	public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
        
    }

}
