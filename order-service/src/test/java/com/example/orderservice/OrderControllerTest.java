package com.example.orderservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateOrderEndpoint() throws Exception {
        String orderJson = "{\"customerId\": 1001, \"amount\": 120.0, \"currency\": \"INR\"}";

        mockMvc.perform(post("/api/orders")
                .contentType("application/json")
                .content(orderJson))
                .andExpect(status().isOk());
    }

    @Test
    void testCancelOrderEndpoint() throws Exception {
        String orderJson = "{\"customerId\": 1002, \"amount\": 150.0, \"currency\": \"USD\"}";

        String response = mockMvc.perform(post("/api/orders")
                .contentType("application/json")
                .content(orderJson))
                .andReturn().getResponse().getContentAsString();

        // Parse JSON to extract id
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response);
        Long id = node.get("id").asLong();

        mockMvc.perform(post("/api/orders/" + id + "/cancel"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetOrderStatusEndpoint() throws Exception {
        String orderJson = "{\"customerId\": 1003, \"amount\": 200.0, \"currency\": \"EUR\"}";

        // Create order
        String response = mockMvc.perform(post("/api/orders")
                .contentType("application/json")
                .content(orderJson))
                .andReturn().getResponse().getContentAsString();

        // Parse JSON to extract id
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response);
        Long id = node.get("id").asLong();

        // Call status endpoint
        mockMvc.perform(get("/api/orders/" + id + "/status"))
                .andExpect(status().isOk());
    }
}


