package com.ryzzlab.e_commerce_engine.controller;

import com.ryzzlab.e_commerce_engine.exception.AppException;
import com.ryzzlab.e_commerce_engine.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/checkout/{subdomain}")
    public ResponseEntity<?> checkout(Principal principal, @PathVariable String subdomain){
        try{
            UUID customerId = UUID.fromString(principal.getName());
            return ResponseEntity.ok(orderService.placeOrder(customerId,subdomain));
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @GetMapping("/{subdomain}")
    public ResponseEntity<?> getOrderHistory(Principal principal, @PathVariable String subdomain){
        try {
            UUID customerId = UUID.fromString(principal.getName());
            return ResponseEntity.ok(orderService.getOrderHistory(customerId,subdomain));
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}
