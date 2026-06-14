package com.ryzzlab.e_commerce_engine.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
public class CartItemResponse {
    UUID cartId;
    UUID productId;
    String productName;
    String slug;
    String imageUrl;
    BigDecimal unitPrice;
    Integer quantity;
    BigDecimal lineTotal;
}
