package ru.kovalenkojuls.usersorders.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import ru.kovalenkojuls.usersorders.service.UserService;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    UserService userService() {
        return Mockito.mock(UserService.class);
    }
}