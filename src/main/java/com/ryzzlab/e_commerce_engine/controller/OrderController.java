package com.ryzzlab.e_commerce_engine.controller;

import com.ryzzlab.e_commerce_engine.dto.UpdateOrderStatusRequest;
import com.ryzzlab.e_commerce_engine.entity.Status;
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
    @GetMapping("/shop/{subdomain}")
    public ResponseEntity<?> getShopOrders(Principal principal, @PathVariable String subdomain){
        try {
            UUID userId = UUID.fromString(principal.getName());
            return ResponseEntity.ok(orderService.getShopOrders(userId,subdomain));
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateStatus(Principal principal,@PathVariable UUID orderId,@RequestBody UpdateOrderStatusRequest request){
        try{
            UUID userId = UUID.fromString(principal.getName());
            Status newStatus = request.getNewStatus();
            return ResponseEntity.ok(orderService.updateOrderStatus(userId,orderId,newStatus));
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}
