package com.ryzzlab.e_commerce_engine;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    String email;
    String password;
    String subdomain;
}
