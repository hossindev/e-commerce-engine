package com.ryzzlab.e_commerce_engine;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse  {
    private String  token;
    public AuthResponse(String token){
        this.token = token;
    }
}
