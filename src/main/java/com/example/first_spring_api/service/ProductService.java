package com.example.first_spring_api.service;

import com.example.first_spring_api.dto.ProductRequest;
import com.example.first_spring_api.exception.NotFoundException;
import com.example.first_spring_api.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    // Lógica Repository em memória para fins didáticos
    private static final List<Product> PRODUCTS = new ArrayList<>();

    static {
        PRODUCTS.add(Product.builder()
                .id(1)
                .name("Notebook")
                .price(new BigDecimal("5000.00"))
                .quantity(10)
                .build());

        PRODUCTS.add(Product.builder()
                .id(2)
                .name("iPhone")
                .price(new BigDecimal("7000.00"))
                .quantity(10)
                .build());

        PRODUCTS.add(Product.builder()
                .id(3)
                .name("Monitor")
                .price(new BigDecimal("500.00"))
                .quantity(10)
                .build());
    }

    public List<Product> findAll(){
        return new ArrayList<>(PRODUCTS);
    }

    public Product create(ProductRequest request){
        int nextId = PRODUCTS.stream()
                .mapToInt(Product::getId)
                .max()
                .orElse(0) + 1;
        Product product = Product.builder()
                .id(nextId)
                .name(request.getName())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build();
        PRODUCTS.add(product);
        return product;
    }

    public Product update(ProductRequest request, Integer id) throws NotFoundException {
        Product product = PRODUCTS.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        return product;
    }

    public void delete(Integer id){
        PRODUCTS.removeIf(p -> p.getId().equals(id));
    }
}
