package com.ryzzlab.e_commerce_engine.controller;

import com.ryzzlab.e_commerce_engine.dto.CreateProductRequest;
import com.ryzzlab.e_commerce_engine.dto.UpdateProductRequest;
import com.ryzzlab.e_commerce_engine.exception.AppException;
import com.ryzzlab.e_commerce_engine.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(Principal principal, @RequestBody CreateProductRequest request){
        try{
            UUID userId = UUID.fromString(principal.getName());
            UUID shopId = UUID.fromString(request.getShopId());
            String name = request.getName();
            String description = request.getDescription();
            String imageUrl = request.getImageUrl();
            BigDecimal price = request.getPrice();
            Integer stockQuantity = request.getStockQuantity();
            return ResponseEntity.ok(productService.createProduct(userId,shopId,name,description,imageUrl,price,stockQuantity));
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(Principal principal, @PathVariable UUID productId, @RequestBody UpdateProductRequest request){

        try{
            UUID userId = UUID.fromString(principal.getName());
            String name = request.getName();
            String description = request.getDescription();
            String imageUrl = request.getImageUrl();
            BigDecimal price = request.getPrice();
            Integer stockQuantity = request.getStockQuantity();
            return ResponseEntity.ok(productService.updateProduct(userId,productId,name,description,imageUrl,price,stockQuantity));
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(Principal principal,@PathVariable UUID productId){
        try{
            UUID userId = UUID.fromString(principal.getName());
            productService.deleteProduct(userId,productId);
            return ResponseEntity.noContent().build();
        }
        catch(AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @GetMapping("/shop/{subdomain}")
    public ResponseEntity<?> getProducts(@PathVariable  String subdomain){
        try{
            return ResponseEntity.ok(productService.getShopProducts(subdomain));
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @GetMapping("/{subdomain}/{slug}")
    public ResponseEntity<?> getProduct(@PathVariable String subdomain,@PathVariable String slug){
        try{
            return ResponseEntity.ok(productService.getProductBySlug(subdomain,slug));
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}
