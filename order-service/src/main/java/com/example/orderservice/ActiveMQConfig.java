package com.example.orderservice;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

/**
 * ActiveMQConfig sets up the ActiveMQ connection factory
 * and configures the redelivery policy.
 *
 * Responsibilities:
 * - Define broker connection (tcp://localhost:61616)
 * - Configure retry count, delay, and exponential backoff
 * - Provide a bean for JMS messaging
 */

@Configuration
@EnableJms
public class ActiveMQConfig {

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        RedeliveryPolicy policy = new RedeliveryPolicy();
        policy.setMaximumRedeliveries(5);          // Retry 5 times
        policy.setInitialRedeliveryDelay(1000);    // 1 second delay
        policy.setBackOffMultiplier(2);            // Exponential backoff double the delay
        policy.setUseExponentialBackOff(true);

        factory.setRedeliveryPolicy(policy);
        return factory;
    }
}

