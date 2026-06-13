package com.ryzzlab.e_commerce_engine;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "cart_items")
public class CartItem  extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartId;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private ShopCustomer shopCustomer;
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private Integer quantity;

}
