package com.ryzzlab.e_commerce_engine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "shop_customers", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"shop_id", "email"})
})
public class ShopCustomer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String passwordHash;
    private String fullName;
}
