package ru.kovalenkojuls.productsorders.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import ru.kovalenkojuls.productsorders.service.OrderService;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    OrderService orderService() {
        return Mockito.mock(OrderService.class);
    }
}