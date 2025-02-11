package ru.kovalenkojuls.productsorders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kovalenkojuls.productsorders.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}