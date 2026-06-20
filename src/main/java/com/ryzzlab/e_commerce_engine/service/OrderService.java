package com.ryzzlab.e_commerce_engine.service;

import com.ryzzlab.e_commerce_engine.dto.OrderItemResponse;
import com.ryzzlab.e_commerce_engine.dto.OrderResponse;
import com.ryzzlab.e_commerce_engine.entity.*;
import com.ryzzlab.e_commerce_engine.exception.AppException;
import com.ryzzlab.e_commerce_engine.repository.ShopCustomerRepository;
import com.ryzzlab.e_commerce_engine.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private ShopCustomerRepository shopCustomerRepository;
    @Autowired
    private ShopRepository shopRepository;
    private OrderItemResponse mapToOrderItemResponse(OrderItem item){
        OrderItemResponse response = new OrderItemResponse();
        response.setProductName(item.getProduct().getName());
        response.setSlug(item.getProduct().getSlug());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setLineTotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        return response;
    }
    private OrderResponse mapToResponse(Order order, List<OrderItem> orderItems){
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setStatus(String.valueOf(order.getStatus()));
        response.setTotalPrice(order.getTotalPrice());
        response.setCreatedAt(order.getCreatedAt());
        response.setOrderItems(orderItems.stream().map(this::mapToOrderItemResponse).toList());
        return response;
    }
    @Transactional
    public OrderResponse placeOrder(UUID customerId,String subdomain){
        ShopCustomer shopCustomer = shopCustomerRepository.findById(customerId).orElseThrow(()->new AppException("user not found",404));
        Shop shop = shopRepository.findBySubdomain(subdomain).orElseThrow(()->new AppException("shop not found"));
    }
}
