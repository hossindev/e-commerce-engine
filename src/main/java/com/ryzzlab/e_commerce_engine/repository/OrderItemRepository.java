package com.ryzzlab.e_commerce_engine.repository;

import com.ryzzlab.e_commerce_engine.entity.Order;
import com.ryzzlab.e_commerce_engine.entity.OrderItem;
import com.ryzzlab.e_commerce_engine.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    List<OrderItem> findAllByOrder(Order order);
    List<OrderItem> findAllByOrderIn(List<Order> orders);
    boolean existsByProduct(Product product);
}
