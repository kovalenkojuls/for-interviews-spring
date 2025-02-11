package ru.kovalenkojuls.productsorders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kovalenkojuls.productsorders.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
