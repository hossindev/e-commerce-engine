package com.ryzzlab.e_commerce_engine.repository;

import com.ryzzlab.e_commerce_engine.entity.Shop;
import com.ryzzlab.e_commerce_engine.entity.ShopCustomization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShopCustomizationRepository extends JpaRepository<ShopCustomization, UUID> {
    Optional<ShopCustomization> findByShop(Shop shop);
}
