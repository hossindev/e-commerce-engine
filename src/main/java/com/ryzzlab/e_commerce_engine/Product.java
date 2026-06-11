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
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;
    @ManyToOne
    @JoinColumn(name = "shopId")
    private Shop shop;
    private String name;
    private String description;
    private String image_url;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price ;
    private Integer stock_quantity;
    private Integer sold;
    private LocalDateTime created_at;
}
