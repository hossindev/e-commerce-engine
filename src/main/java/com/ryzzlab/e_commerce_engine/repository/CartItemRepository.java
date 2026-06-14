package com.ryzzlab.e_commerce_engine.repository;

import com.ryzzlab.e_commerce_engine.entity.CartItem;
import com.ryzzlab.e_commerce_engine.entity.Product;
import com.ryzzlab.e_commerce_engine.entity.Shop;
import com.ryzzlab.e_commerce_engine.entity.ShopCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    Optional<CartItem>  findByShopCustomerAndShopAndProduct(ShopCustomer customer, Shop shop, Product product);
    List<CartItem> findAllByShopCustomerAndShop(ShopCustomer customer, Shop shop);
    List<CartItem> findAllByShopCustomer_IdAndShop(UUID customerId, Shop shop);
}
