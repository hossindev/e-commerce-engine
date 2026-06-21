package com.ryzzlab.e_commerce_engine.dto;

import com.ryzzlab.e_commerce_engine.entity.ShopCustomer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse {
    private String street;
    private String city;
    private String postalCode;
    private String country ;
    private Boolean isDefault;
}
