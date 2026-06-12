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
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;
    @ManyToOne
    @JoinColumn(name = "shopId")
    private Shop shop;
    private String name;
    private String description;
    private String imageUrl;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price ;
    private Integer stockQuantity;
    private Integer sold;

}
