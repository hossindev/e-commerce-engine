package com.ryzzlab.e_commerce_engine.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemResponse {
    String productName;
    String slug;
    Integer quantity;
    BigDecimal unitPrice;
    BigDecimal lineTotal;
}
