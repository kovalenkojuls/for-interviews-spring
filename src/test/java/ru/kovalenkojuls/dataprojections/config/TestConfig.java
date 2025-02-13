package ru.kovalenkojuls.dataprojections.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import ru.kovalenkojuls.dataprojections.service.EmployeeService;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    EmployeeService employeeService() {
        return Mockito.mock(EmployeeService.class);
    }
}