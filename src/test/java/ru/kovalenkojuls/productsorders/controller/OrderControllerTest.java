package ru.kovalenkojuls.productsorders.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.kovalenkojuls.productsorders.config.TestConfig;
import ru.kovalenkojuls.productsorders.domain.Customer;
import ru.kovalenkojuls.productsorders.domain.Order;
import ru.kovalenkojuls.productsorders.domain.Product;
import ru.kovalenkojuls.productsorders.service.OrderService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@Import(TestConfig.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);;

    @Test
    void getOrderById_Success() throws Exception {
        Long orderId = 1L;
        Order order = new Order();
        order.setOrderId(orderId);

        when(orderService.getOrderById(orderId)).thenReturn(Optional.of(order));

        MvcResult result = mockMvc.perform(get("/api/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String expectedJson = objectMapper.writeValueAsString(order);
        String actualJson = result.getResponse().getContentAsString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void getOrderById_notFound() throws Exception {
        Long orderId = 1L;
        when(orderService.getOrderById(orderId)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createOrder() throws Exception {

        Customer customer = new Customer();
        customer.setFirstName("julia");
        customer.setLastName("kovalenko");
        customer.setEmail("julia@meil.ru");
        customer.setContactNumber("89998887766");

        Product product1 = new Product();
        product1.setProductId(1L);

        Product product2 = new Product();
        product2.setProductId(2L);

        Set<Product> products = new HashSet<>();
        products.add(product1);
        products.add(product2);

        Order order = new Order();
        order.setCustomer(customer);
        order.setProducts(products);
        order.setOrderDate(new Date());
        order.setShippingAddress("moscow");
        order.setTotalPrice(BigDecimal.valueOf(100.50));
        order.setOrderStatus("PENDING");

        Order savedOrder = new Order();
        savedOrder.setOrderId(1L);
        savedOrder.setCustomer(customer);
        savedOrder.setProducts(products);
        savedOrder.setOrderDate(order.getOrderDate());
        savedOrder.setShippingAddress(order.getShippingAddress());
        savedOrder.setTotalPrice(order.getTotalPrice());
        savedOrder.setOrderStatus(order.getOrderStatus());

        when(orderService.save(any(Order.class))).thenReturn(savedOrder);

        String jsonOrder = objectMapper.writeValueAsString(order);

        MvcResult result = mockMvc.perform(post("/api/orders")
                        .content(jsonOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String expectedJson = objectMapper.writeValueAsString(savedOrder);
        String actualJson = result.getResponse().getContentAsString();

        assertEquals(expectedJson, actualJson);
    }

}