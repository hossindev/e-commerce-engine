package com.ryzzlab.e_commerce_engine.controller;

import com.ryzzlab.e_commerce_engine.dto.AddToCartRequest;
import com.ryzzlab.e_commerce_engine.dto.UpdateCartRequest;
import com.ryzzlab.e_commerce_engine.exception.AppException;
import com.ryzzlab.e_commerce_engine.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(Principal principal, @RequestBody AddToCartRequest request){
        try {
            UUID customerId = UUID.fromString(principal.getName());
            String subdomain = request.getSubdomain();
            String slug = request.getSlug();
            Integer quantity = request.getQuantity();
            return ResponseEntity.ok(cartService.addToCart(customerId,subdomain,slug,quantity));
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @PutMapping("/update/{cartId}")
    public ResponseEntity<?> updateCart(Principal principal,@PathVariable UUID cartId,@RequestBody UpdateCartRequest request){
        try{
            UUID customerId = UUID.fromString(principal.getName());
            Integer quantity = request.getQuantity();
            if(quantity <= 0){
                cartService.removeFromCart(customerId,cartId);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(cartService.updateCartItem(customerId,cartId,quantity));
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable UUID cartId,Principal principal){
        try{
            UUID customerId = UUID.fromString(principal.getName());
            cartService.removeFromCart(customerId,cartId);
            return ResponseEntity.noContent().build();
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @GetMapping("/{subdomain}")
    public ResponseEntity<?> getCart(@PathVariable String subdomain,Principal principal){
        try{
            UUID customerId = UUID.fromString(principal.getName());
            return ResponseEntity.ok(cartService.getCart(customerId,subdomain));
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}
