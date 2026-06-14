package com.ryzzlab.e_commerce_engine.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateProductRequest {
    String name;
    String description;
    String imageUrl;
    BigDecimal price;
    Integer stockQuantity;
}
