package com.ryzzlab.e_commerce_engine.controller;

import com.ryzzlab.e_commerce_engine.dto.CreateShopRequest;
import com.ryzzlab.e_commerce_engine.dto.UpdateCustomizationRequest;
import com.ryzzlab.e_commerce_engine.exception.AppException;
import com.ryzzlab.e_commerce_engine.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/shops")
public class ShopController {
    @Autowired
    private ShopService shopService;
    @PostMapping("/create")
    public ResponseEntity<?> createShop(Principal principal, @RequestBody CreateShopRequest request ){
        try {
            UUID userId = UUID.fromString(principal.getName());
            String name = request.getName();
            String subdomain = request.getSubdomain();
            String templateName = request.getTemplateName();
            String description = request.getDescription();
            return ResponseEntity.ok(shopService.createShop(userId,name,subdomain,templateName,description));
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @PutMapping("/customization")
    public ResponseEntity<?> customize(Principal principal, @RequestBody UpdateCustomizationRequest request){
        try {
            UUID userId = UUID.fromString(principal.getName());
            UUID shopId = UUID.fromString(request.getShopId());
            String logoUrl = request.getLogoUrl();
            String bannerUrl = request.getBannerUrl();
            String tagline = request.getTagline();
            String primaryColor = request.getPrimaryColor();
            String secondaryColor = request.getSecondaryColor();
            String font = request.getFont();
            return ResponseEntity.ok(shopService.updateCustomization(shopId,userId, logoUrl,bannerUrl, tagline, primaryColor,secondaryColor,font ));
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @GetMapping("/{subdomain}")
    public ResponseEntity<?> getShop(@PathVariable String subdomain){
        try{
            return ResponseEntity.ok(shopService.getShopBySubdomain(subdomain));
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}
