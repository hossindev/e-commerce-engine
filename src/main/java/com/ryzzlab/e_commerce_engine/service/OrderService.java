package com.ryzzlab.e_commerce_engine.service;

import com.ryzzlab.e_commerce_engine.dto.OrderItemResponse;
import com.ryzzlab.e_commerce_engine.dto.OrderResponse;
import com.ryzzlab.e_commerce_engine.entity.*;
import com.ryzzlab.e_commerce_engine.exception.AppException;
import com.ryzzlab.e_commerce_engine.repository.*;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private ShopCustomerRepository shopCustomerRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private StripeService stripeService;
    private OrderItemResponse mapToOrderItemResponse(OrderItem item) {
        OrderItemResponse response = new OrderItemResponse();
        response.setProductName(item.getProduct().getName());
        response.setSlug(item.getProduct().getSlug());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setLineTotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        return response;
    }

    private OrderResponse mapToResponse(Order order, List<OrderItem> orderItems) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setStatus(String.valueOf(order.getStatus()));
        response.setTotalPrice(order.getTotalPrice());
        response.setCreatedAt(order.getCreatedAt());
        response.setOrderItems(orderItems.stream().map(this::mapToOrderItemResponse).toList());
        return response;
    }

    @Transactional
    public OrderResponse placeOrder(UUID customerId, String subdomain,UUID addressId) {
        ShopCustomer shopCustomer = shopCustomerRepository.findById(customerId).orElseThrow(() -> new AppException("user not found", 404));
        Shop shop = shopRepository.findBySubdomain(subdomain).orElseThrow(() -> new AppException("shop not found", 404));
        List<CartItem> cart = cartItemRepository.findAllByShopCustomerAndShop(shopCustomer, shop);
        if (cart.isEmpty()) {
            throw new AppException("Cart cant be empty", 400);
        }
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem item : cart) {
            if (item.getQuantity() > item.getProduct().getStockQuantity() || item.getQuantity() <= 0) {
                throw new AppException("Not enough stock available", 409);
            }
            totalPrice = totalPrice.add(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        PaymentIntent paymentIntent = stripeService.createAndConfirmPaymentIntent(totalPrice, "pm_card_visa");
        Order order = new Order();
        order.setShop(shop);
        order.setStripePaymentIntentId(paymentIntent.getId());
        order.setShopCustomer(shopCustomer);
        order.setStatus(Status.PENDING);
        order.setTotalPrice(totalPrice);
        order.setAddress(addressRepository.findById(addressId).orElseThrow(()->new AppException("address not set",400)));
        Order savedOrder = orderRepository.save(order);
        List<OrderItem> orderItems = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        for (CartItem item : cart) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(item.getProduct().getPrice());
            orderItemRepository.save(orderItem);
            orderItems.add(orderItem);
            item.getProduct().setSold(item.getProduct().getSold() + item.getQuantity());
            item.getProduct().setStockQuantity(item.getProduct().getStockQuantity() - item.getQuantity());
            products.add(item.getProduct());
        }
        productRepository.saveAll(products);
        cartItemRepository.deleteAll(cart);
        return mapToResponse(savedOrder, orderItems);
    }
    public List<OrderResponse> getOrderHistory(UUID customerId, String subdomain){
        ShopCustomer customer = shopCustomerRepository.findByIdAndShopSubdomain(customerId,subdomain).orElseThrow(()->new AppException("forbidden",403));
        List<Order> orders = orderRepository.findAllByShopCustomer(customer);
        List<OrderItem> allItems = orderItemRepository.findAllByOrderIn(orders);
        Map<UUID, List<OrderItem>> itemsByOrder = allItems.stream()
                .collect(Collectors.groupingBy(item -> item.getOrder().getOrderId()));
        List<OrderResponse> response = new ArrayList<>();
        for(Order order : orders){
            List<OrderItem> items = itemsByOrder.getOrDefault(order.getOrderId(), new ArrayList<>());
            response.add(mapToResponse(order, items));;
        }
        return response;
    }
    public List<OrderResponse> getShopOrders(UUID userId,String subdomain){
        Shop shop = shopRepository.findBySubdomain(subdomain).orElseThrow(()->new AppException("Shop not found",404));
        if(!shop.getUser().getUserId().equals(userId)) throw new AppException("forbidden",403);
        List<Order> orders = orderRepository.findAllByShop(shop);
        List<OrderItem> allItems = orderItemRepository.findAllByOrderIn(orders);
        Map<UUID,List<OrderItem>> itemsByOrder = allItems.stream()
                .collect(Collectors.groupingBy(item -> item.getOrder().getOrderId()));
        List<OrderResponse> responses = new ArrayList<>();
        for(Order order : orders){
            List<OrderItem> items = itemsByOrder.getOrDefault(order.getOrderId(), new ArrayList<>());
            responses.add(mapToResponse(order,items));
        }
        return responses;
    }
    public OrderResponse updateOrderStatus(UUID userId,UUID orderId,Status newStatus){
        Order order = orderRepository.findById(orderId).orElseThrow(()->new AppException("Order not found",404));
        if(!order.getShop().getUser().getUserId().equals(userId)) throw new AppException("forbidden",403);
        order.setStatus(newStatus);
        orderRepository.save(order);
        List<OrderItem> items = orderItemRepository.findAllByOrder(order);
        return mapToResponse(order,items);
    }
}
