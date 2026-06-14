package com.ryzzlab.e_commerce_engine.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateProductRequest {
    String  shopId;
    String name;
    String description;
    String imageUrl;
    BigDecimal price;
    Integer stockQuantity;
}
