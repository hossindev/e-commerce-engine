package com.ryzzlab.e_commerce_engine.service;

import com.ryzzlab.e_commerce_engine.dto.AddressResponse;
import com.ryzzlab.e_commerce_engine.dto.OrderResponse;
import com.ryzzlab.e_commerce_engine.entity.Address;
import com.ryzzlab.e_commerce_engine.entity.Order;
import com.ryzzlab.e_commerce_engine.entity.OrderItem;
import com.ryzzlab.e_commerce_engine.entity.ShopCustomer;
import com.ryzzlab.e_commerce_engine.exception.AppException;
import com.ryzzlab.e_commerce_engine.repository.AddressRepository;
import com.ryzzlab.e_commerce_engine.repository.ShopCustomerRepository;
import com.ryzzlab.e_commerce_engine.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ShopCustomerRepository shopCustomerRepository;
    @Autowired
    private ShopRepository shopRepository;
    private AddressResponse mapToResponse(Address address){
        AddressResponse response = new AddressResponse();
        response.setAddressId(address.getAddressId());
        response.setStreet(address.getStreet());
        response.setCity(address.getCity());
        response.setPostalCode(address.getPostalCode());
        response.setCountry(address.getCountry());
        response.setIsDefault(address.getIsDefault());
        return response;
    }
    public AddressResponse createAddress(UUID customerId,String subdomain,String street,String city,String postalCode,String country,Boolean isDefault){
        ShopCustomer customer =  shopCustomerRepository.findByIdAndShopSubdomain(customerId,subdomain).orElseThrow(()-> new AppException("forbidden",403));
        Address address = new Address();
        address.setShopCustomer(customer);
        address.setStreet(street);
        address.setCity(city);
        address.setPostalCode(postalCode);
        address.setCountry(country);
        if(Boolean.TRUE.equals(isDefault)){
            List<Address> addresses = addressRepository.findAllByShopCustomer(customer);
            for(Address address1 : addresses){
                if(Boolean.TRUE.equals(address1.getIsDefault())){
                    address1.setIsDefault(false);
                    addressRepository.save(address1);
                }
            }
        }
        address.setIsDefault(isDefault);
        addressRepository.save(address);
        return mapToResponse(address);
    }
    public AddressResponse updateAddress(UUID customerId,String subdomain,UUID addressId,String street,String city,String postalCode,String country,Boolean isDefault){
        ShopCustomer customer =  shopCustomerRepository.findByIdAndShopSubdomain(customerId,subdomain).orElseThrow(()-> new AppException("forbidden",403));
        Address address = addressRepository.findByAddressIdAndShopCustomer(addressId, customer).orElseThrow(()->new AppException("address not found or unauthorized", 404));
        if(street != null)address.setStreet(street);
        if(city != null)address.setCity(city);
        if( postalCode != null)address.setPostalCode(postalCode);
        if( country!= null)address.setCountry(country);

        if(Boolean.TRUE.equals(isDefault)){
            List<Address> addresses = addressRepository.findAllByShopCustomer(customer);
            for(Address address1 : addresses){
                if(Boolean.TRUE.equals(address1.getIsDefault())){
                    address1.setIsDefault(false);
                    addressRepository.save(address1);
                }
            }
            address.setIsDefault(isDefault);
        }
        addressRepository.save(address);
        return mapToResponse(address);
    }
    public void deleteAddress(UUID customerId,String subdomain,UUID addressId){
        ShopCustomer customer =  shopCustomerRepository.findByIdAndShopSubdomain(customerId,subdomain).orElseThrow(()-> new AppException("forbidden",403));
        Address address = addressRepository.findByAddressIdAndShopCustomer(addressId, customer).orElseThrow(()->new AppException("address not found or unauthorized", 404));
        addressRepository.delete(address);
    }
    public List<AddressResponse> getAllAddresses(UUID customerId,String subdomain){
        ShopCustomer customer =  shopCustomerRepository.findByIdAndShopSubdomain(customerId,subdomain).orElseThrow(()-> new AppException("forbidden",403));
        List<Address> addresses= addressRepository.findAllByShopCustomer(customer);
        List<AddressResponse> response = new ArrayList<>();
        for(Address address : addresses){
            response.add(mapToResponse(address));
        }
        return response;
    }
    public AddressResponse getAddress(UUID customerId,String subdomain,UUID addressId){
        ShopCustomer customer =  shopCustomerRepository.findByIdAndShopSubdomain(customerId,subdomain).orElseThrow(()-> new AppException("forbidden",403));
        Address address = addressRepository.findByAddressIdAndShopCustomer(addressId, customer).orElseThrow(()->new AppException("address not found or unauthorized", 404));
        return mapToResponse(address);
    }
}
