package com.example.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Entry point for the Order Service Spring Boot application.
 *
 * Responsibilities:
 * - Bootstraps the Spring context
 * - Starts embedded Tomcat server
 * - Initializes all beans and configurations
 */

@SpringBootApplication
@EnableScheduling   // enables the OutboxPublisher @Scheduled job
@EnableJms
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
        
    }
}
