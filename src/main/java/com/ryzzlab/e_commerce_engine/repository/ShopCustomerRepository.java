package com.ryzzlab.e_commerce_engine.repository;

import com.ryzzlab.e_commerce_engine.entity.Shop;
import com.ryzzlab.e_commerce_engine.entity.ShopCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShopCustomerRepository extends JpaRepository<ShopCustomer, UUID> {

    Optional<ShopCustomer> findByEmailAndShop(String email, Shop shop);
    Optional<ShopCustomer> findByIdAndShopSubdomain(UUID customerId,String subdomain);
}
