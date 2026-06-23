package com.example.first_spring_api.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductRequest {
    private String name;
    private BigDecimal price;
    private Integer quantity;
}
