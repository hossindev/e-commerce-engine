package com.ryzzlab.e_commerce_engine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequest {
    String subdomain;
    String slug;
    Integer quantity;
}