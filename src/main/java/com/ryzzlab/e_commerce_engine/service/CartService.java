package com.ryzzlab.e_commerce_engine.service;

import com.ryzzlab.e_commerce_engine.dto.CartItemResponse;
import com.ryzzlab.e_commerce_engine.entity.CartItem;
import com.ryzzlab.e_commerce_engine.entity.Product;
import com.ryzzlab.e_commerce_engine.entity.Shop;
import com.ryzzlab.e_commerce_engine.entity.ShopCustomer;
import com.ryzzlab.e_commerce_engine.exception.AppException;
import com.ryzzlab.e_commerce_engine.repository.CartItemRepository;
import com.ryzzlab.e_commerce_engine.repository.ProductRepository;
import com.ryzzlab.e_commerce_engine.repository.ShopCustomerRepository;
import com.ryzzlab.e_commerce_engine.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {
    @Autowired
    private ShopCustomerRepository shopCustomerRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    private CartItemResponse mapToResponse(CartItem cartItem){
        Product product = cartItem.getProduct();
        CartItemResponse response = new CartItemResponse();
        response.setCartId(cartItem.getCartId());
        response.setProductId(product.getProductId());
        response.setProductName(product.getName());
        response.setSlug(product.getSlug());
        response.setImageUrl(product.getImageUrl());
        response.setUnitPrice(product.getPrice());
        response.setQuantity(cartItem.getQuantity());
        response.setLineTotal(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        return response;
    }
    public CartItemResponse addToCart(UUID customerId,String subdomain,String  slug,Integer quantity){
        if (quantity <= 0) throw new AppException("Quantity must be positive", 400);
        Shop shop = shopRepository.findBySubdomain(subdomain).orElseThrow(() -> new AppException("Shop not found", 404));
        Product product = productRepository.findByShopAndSlug(shop, slug).orElseThrow(() -> new AppException("Product not found", 404));
        ShopCustomer customer = shopCustomerRepository.findById(customerId).orElseThrow(()->new AppException("User not found",404));
        Optional<CartItem> existing= cartItemRepository.findByShopCustomerAndShopAndProduct(customer,shop,product);
        CartItem cartItem;
        int newQuantity;
        if(existing.isPresent()){
            cartItem = existing.get();
            newQuantity = cartItem.getQuantity() + quantity;
        }
        else{
            cartItem = new CartItem();
            cartItem.setShopCustomer(customer);
            cartItem.setShop(shop);
            cartItem.setProduct(product);
            newQuantity = quantity;
        }
        if (newQuantity > product.getStockQuantity()) throw new AppException("Not enough stock available", 409);
        cartItem.setQuantity(newQuantity);
        cartItemRepository.save(cartItem);
        return mapToResponse(cartItem);
    }

    public CartItemResponse updateCartItem(UUID customerId,UUID cartId,Integer quantity){
        if (quantity <= 0) throw new AppException("Quantity must be positive", 400);
        CartItem cartItem = cartItemRepository.findById(cartId).orElseThrow(()-> new AppException("Cart not found",404));
        if(!cartItem.getShopCustomer().getId().equals(customerId)) throw new AppException("forbidden",403);
        if(quantity > cartItem.getProduct().getStockQuantity()) throw new AppException("Not enough stock available", 409);
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        return  mapToResponse(cartItem);
    }
    public void removeFromCart(UUID customerId, UUID cartId){
        CartItem cartItem = cartItemRepository.findById(cartId).orElseThrow(()-> new AppException("Cart not found",404));
        if(!cartItem.getShopCustomer().getId().equals(customerId)) throw new AppException("forbidden",403);
        cartItemRepository.delete(cartItem);
    }
    public List<CartItemResponse> getCart(UUID customerId,String subdomain){
        Shop shop = shopRepository.findBySubdomain(subdomain).orElseThrow(()->new AppException("Shop not found",404));
        List<CartItem> cart = cartItemRepository.findAllByShopCustomer_IdAndShop(customerId,shop);
        return cart.stream()
                .map(this::mapToResponse)
                .toList();
    }
}
