package com.ryzzlab.e_commerce_engine;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private ShopCustomer shopCustomer;
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice ;

}
