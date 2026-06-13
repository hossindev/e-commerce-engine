package com.ryzzlab.e_commerce_engine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateShopRequest {
    String name;
    String subdomain;
    String description;
    String templateName;
}
