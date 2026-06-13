package com.ryzzlab.e_commerce_engine;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    String email;
    String password;
    String fullName;
    String subdomain;
}
