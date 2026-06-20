package com.ryzzlab.e_commerce_engine.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderResponse  {
    UUID orderId;
    String status;
    BigDecimal totalPrice;
    LocalDateTime createdAt;
    List<OrderItemResponse> orderItems;
}
