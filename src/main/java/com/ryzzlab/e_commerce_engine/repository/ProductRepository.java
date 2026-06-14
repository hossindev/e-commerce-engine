package com.ryzzlab.e_commerce_engine.repository;

import com.ryzzlab.e_commerce_engine.entity.Product;
import com.ryzzlab.e_commerce_engine.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllProductsByShop(Shop shop);
    Optional<Product> findByShopAndSlug(Shop shop,String slug);
}
