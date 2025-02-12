package ru.kovalenkojuls.jdbclibrary.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import ru.kovalenkojuls.jdbclibrary.service.BookService;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    BookService bookService() {
        return Mockito.mock(BookService.class);
    }
}
