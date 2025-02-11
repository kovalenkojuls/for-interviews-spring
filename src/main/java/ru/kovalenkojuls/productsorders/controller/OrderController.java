package ru.kovalenkojuls.productsorders.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kovalenkojuls.productsorders.domain.Order;
import ru.kovalenkojuls.productsorders.service.OrderService;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public OrderController(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOrderById(@PathVariable Long id) throws JsonProcessingException {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            String json = objectMapper.writeValueAsString(order.get());
            return ResponseEntity.ok(json);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody String jsonOrder) throws JsonProcessingException {
        Order order = objectMapper.readValue(jsonOrder, Order.class);
        String json = objectMapper.writeValueAsString(orderService.save(order));
        return ResponseEntity.ok(json);
    }
}
