package ru.kovalenkojuls.productsorders.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kovalenkojuls.productsorders.domain.Product;
import ru.kovalenkojuls.productsorders.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(p -> {
                    p.setDescription(updatedProduct.getDescription());
                    p.setName(updatedProduct.getName());
                    p.setPrice(updatedProduct.getPrice());
                    p.setQuantityInStock(updatedProduct.getQuantityInStock());
                    return productRepository.save(p);
                });
    }

    public boolean deleteProduct(Long id) {
        return  productRepository.findById(id)
                .map(p -> {
                    productRepository.delete(p);
                    return true;
                }).orElse(false);
    }
}
