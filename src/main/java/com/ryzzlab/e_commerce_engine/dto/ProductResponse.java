package com.ryzzlab.e_commerce_engine.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ProductResponse {
    UUID productId;
    String slug;
    String name;
    String description;
    String imageUrl;
    BigDecimal price;
    Integer stockQuantity;
    Integer sold;
}
