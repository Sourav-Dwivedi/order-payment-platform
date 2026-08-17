package com.example.orderservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.common.dto.OrderStatus;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;

@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testSaveAndFindOrder() {
        Order order = new Order();
        order.setCustomerId(1001L);
        order.setAmount(100.0);
        order.setCurrency("INR");
        order.setStatus(OrderStatus.PENDING.name());

        Order saved = orderRepository.save(order);

        assertThat(orderRepository.findById(saved.getId())).isPresent();
    }
}

