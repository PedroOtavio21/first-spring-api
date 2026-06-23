package com.example.first_spring_api.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Product {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer quantity;
}
