package com.ryzzlab.e_commerce_engine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ShopResponse {
    UUID shopId;
    String name;
    String subdomain;
    String description;
    String templateName;
    String logoUrl;
    String bannerUrl;
    String tagline;
    String primaryColor;
    String secondaryColor;
    String font;
}
