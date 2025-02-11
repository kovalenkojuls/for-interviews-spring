package ru.kovalenkojuls.productsorders.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kovalenkojuls.productsorders.domain.Customer;
import ru.kovalenkojuls.productsorders.domain.Order;
import ru.kovalenkojuls.productsorders.domain.Product;
import ru.kovalenkojuls.productsorders.repository.CustomerRepository;
import ru.kovalenkojuls.productsorders.repository.OrderRepository;
import ru.kovalenkojuls.productsorders.repository.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public Order save(Order order) {
        setCustomer(order);
        setProducts(order);
        order.setOrderDate(new Date());
        return orderRepository.save(order);
    }

    private void setProducts(Order order) {
        Set<Long> productIds = order.getProducts().stream()
                .map(Product::getProductId)
                .collect(Collectors.toSet());
        List<Product> products = productRepository.findAllById(productIds);

        if (products.size() != productIds.size()) {
            throw new IllegalArgumentException("Not all products found");
        }
        order.setProducts(new HashSet<>(products));
    }

    private void setCustomer(Order order) {
        Customer customer = order.getCustomer();
        if (customer.getCustomerId() != null) {
            order.setCustomer(customerRepository.findById(customer.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer with ID " + customer.getCustomerId() + " not found")));
        } else {
            Customer savedCustomer = customerRepository.save(customer);
            order.setCustomer(savedCustomer);
        }
    }
}
