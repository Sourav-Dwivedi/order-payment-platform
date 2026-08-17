package com.example.orderservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.common.dto.OrderStatus;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testCreateOrderPublishesEvent() {
        Order order = new Order();
        order.setCustomerId(2001L);
        order.setAmount(200.0);
        order.setCurrency("USD");

        Order saved = orderService.createOrder(order);

        assertThat(orderRepository.findById(saved.getId())).isPresent();
        assertThat(saved.getStatus()).isEqualTo(OrderStatus.PAYMENT_PROCESSING.name());
    }

    @Test
    void testCancelOrderUpdatesStatus() {
        Order order = new Order();
        order.setCustomerId(2002L);
        order.setAmount(300.0);
        order.setCurrency("EUR");
        order.setStatus(OrderStatus.PENDING.name());

        Order saved = orderRepository.save(order);

        Order cancelled = orderService.cancelOrder(saved.getId());
        assertThat(cancelled.getStatus()).isEqualTo(OrderStatus.CANCELLED.name());
    }

    @Test
    void testGetOrderStatus() {
        Order order = new Order();
        order.setCustomerId(2003L);
        order.setAmount(400.0);
        order.setCurrency("SGD");
        order.setStatus(OrderStatus.PENDING.name());

        Order saved = orderRepository.save(order);

        String status = orderService.getOrderStatus(saved.getId());
        assertThat(status).isEqualTo(OrderStatus.PENDING.name());
    }
}
