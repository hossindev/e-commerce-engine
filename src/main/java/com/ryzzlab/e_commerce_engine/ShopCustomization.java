package com.ryzzlab.e_commerce_engine;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "shop_customization")
public class ShopCustomization extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID shop_customization_id;
    @OneToOne
    @JoinColumn(name = "shop_id", unique = true)
    private Shop shop;
    private String logoUrl;
    private String bannerUrl;
    private String tagline;
    private String primaryColor;
    private String secondaryColor;
    private String font;

}
