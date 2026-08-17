package com.example.paymentservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.paymentservice.controller.PaymentController;
import com.example.paymentservice.repository.PaymentTransactionRepository;

/**
 * Unit tests for PaymentController.
 *
 * Responsibilities:
 * - Validate REST endpoints for payment processing
 * - Ensure correct HTTP responses and error handling
 */

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private PaymentTransactionRepository repository;

    @Test
    void testCreatePayment() throws Exception {
        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"orderId\":123,\"amount\":200.0,\"currency\":\"USD\"}"))
                .andExpect(status().isOk());
    }
}
