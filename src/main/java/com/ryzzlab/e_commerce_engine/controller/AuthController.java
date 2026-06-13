package com.ryzzlab.e_commerce_engine.controller;

import com.ryzzlab.e_commerce_engine.exception.AppException;
import com.ryzzlab.e_commerce_engine.dto.AuthResponse;
import com.ryzzlab.e_commerce_engine.dto.LoginRequest;
import com.ryzzlab.e_commerce_engine.dto.RegisterRequest;
import com.ryzzlab.e_commerce_engine.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/owner/register")
    public ResponseEntity<?> registerOwner(@RequestBody RegisterRequest request){
        try {
            String token = authService.registerOwner(request.getEmail(), request.getPassword(), request.getFullName());
            AuthResponse response = new AuthResponse(token);
            return ResponseEntity.ok(response);
        }
        catch (AppException e ){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @PostMapping("/owner/login")
    public ResponseEntity<?> loginOwner(@RequestBody LoginRequest request){
        try{
            String token = authService.loginOwner(request.getEmail(), request.getPassword());
            AuthResponse response = new AuthResponse(token);
            return ResponseEntity.ok(response);
        }
        catch (AppException e ){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @PostMapping("/customer/register")
    public ResponseEntity<?> registerCustomer(@RequestBody RegisterRequest request){
        try {
            String token = authService.registerBuyer(request.getEmail(), request.getPassword(), request.getFullName(), request.subdomain);
            AuthResponse response = new AuthResponse(token);
            return ResponseEntity.ok(response);
        }
        catch (AppException e ){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @PostMapping("/customer/login")
    public ResponseEntity<?> loginCustomer(@RequestBody LoginRequest request){
        try{
            String token = authService.loginBuyer(request.getEmail(), request.getPassword(), request.subdomain);
            AuthResponse response = new AuthResponse(token);
            return ResponseEntity.ok(response);
        }
        catch (AppException e ){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}
