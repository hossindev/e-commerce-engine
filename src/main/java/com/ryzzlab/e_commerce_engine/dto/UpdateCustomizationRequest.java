package com.ryzzlab.e_commerce_engine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCustomizationRequest {
    String shopId;
    String logoUrl;
    String bannerUrl;
    String tagline;
    String primaryColor;
    String secondaryColor;
    String font;
}
