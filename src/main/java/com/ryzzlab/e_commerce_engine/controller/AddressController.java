package com.ryzzlab.e_commerce_engine.controller;

import com.ryzzlab.e_commerce_engine.dto.CreateAddressRequest;
import com.ryzzlab.e_commerce_engine.exception.AppException;
import com.ryzzlab.e_commerce_engine.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    @Autowired
    private AddressService addressService;
    @PostMapping("/{subdomain}")
    public ResponseEntity<?> createAddress(Principal principal, @PathVariable String subdomain, @RequestBody CreateAddressRequest request){
        try{
            UUID customerId = UUID.fromString(principal.getName());
            String street = request.getStreet();
            String city = request.getCity();
            String postalCode = request.getPostalCode();
            String country = request.getCountry();
            Boolean isDefault = request.getIsDefault();
            return ResponseEntity.ok(addressService.createAddress(customerId,subdomain,street,city,postalCode,country,isDefault));
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @PutMapping("/update/{subdomain}")
    public ResponseEntity<?> updateAddress(Principal principal, @PathVariable String subdomain, @RequestBody CreateAddressRequest request){
        try{
            UUID customerId = UUID.fromString(principal.getName());
            UUID addressId = request.getAddressId();
            String street = request.getStreet();
            String city = request.getCity();
            String postalCode = request.getPostalCode();
            String country = request.getCountry();
            Boolean isDefault = request.getIsDefault();
            return ResponseEntity.ok(addressService.updateAddress(customerId,subdomain,addressId,street,city,postalCode,country,isDefault));
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{subdomain}")
    public ResponseEntity<?> deleteAddress(Principal principal,@PathVariable String subdomain,UUID addressId){
        try{
            UUID customerId = UUID.fromString(principal.getName());
            addressService.deleteAddress(customerId,subdomain,addressId);
            return ResponseEntity.noContent().build();
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @GetMapping("/get/all/{subdomain}")
    public ResponseEntity<?> getAllAddresses(Principal principal,@PathVariable String subdomain){
        try{
            UUID customerId = UUID.fromString(principal.getName());
            return ResponseEntity.ok(addressService.getAllAddresses(customerId,subdomain));
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @GetMapping("/get/{subdomain}")
    public ResponseEntity<?> getAddress(Principal principal,@PathVariable String subdomain,UUID addressId){
        try {
            UUID customerId = UUID.fromString(principal.getName());
            return ResponseEntity.ok(addressService.getAddress(customerId,subdomain,addressId));
        }
        catch (AppException e){
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}
