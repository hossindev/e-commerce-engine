package com.ryzzlab.e_commerce_engine;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShopCustomerRepository extends JpaRepository<ShopCustomer, UUID> {

    Optional<ShopCustomer> findByEmailAndShop(String email, Shop shop);
}
