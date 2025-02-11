package ru.kovalenkojuls.productsorders.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kovalenkojuls.productsorders.domain.Product;
import ru.kovalenkojuls.productsorders.service.ProductService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    public ProductController(ProductService productService, ObjectMapper objectMapper) {
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<String> getAllProducts() throws JsonProcessingException {
        List<Product> products = productService.getAllProducts();
        String json = objectMapper.writeValueAsString(products);
        return ResponseEntity.ok(json);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable Long id) throws JsonProcessingException {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            String json = objectMapper.writeValueAsString(product.get());
            return ResponseEntity.ok(json);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody String productJson) throws JsonProcessingException {
        Product product = objectMapper.readValue(productJson, Product.class);
        Product savedProduct = productService.save(product);
        String savedProductJson = objectMapper.writeValueAsString(savedProduct);
        return new ResponseEntity<>(savedProductJson, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody String productJson) throws JsonProcessingException {
        Product updatedProduct = objectMapper.readValue(productJson, Product.class);
        Optional<Product> optionalProduct = productService.updateProduct(id, updatedProduct);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Product updated successfully");
            response.put("product", product);
            String savedProductJson = objectMapper.writeValueAsString(response);
            return new ResponseEntity<>(savedProductJson, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) throws JsonProcessingException {
        if (productService.deleteProduct(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Product deleted successfully");
            response.put("id", id);
            String savedProductJson = objectMapper.writeValueAsString(response);
            return new ResponseEntity<>(savedProductJson, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
