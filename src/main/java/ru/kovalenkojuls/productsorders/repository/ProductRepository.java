package ru.kovalenkojuls.productsorders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kovalenkojuls.productsorders.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}