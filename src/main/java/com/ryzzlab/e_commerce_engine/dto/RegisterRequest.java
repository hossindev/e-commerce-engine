package com.ryzzlab.e_commerce_engine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {
    public String email;
    public String password;
    public String fullName;
    public String subdomain;
}
