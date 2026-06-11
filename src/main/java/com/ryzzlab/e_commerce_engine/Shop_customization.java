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
public class Shop_customization {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID shop_customization_id;
    @ManyToOne
    @JoinColumn(name = "shopId")
    private Shop shop;
    private String logo_url;
    private String banner_url;
    private String tagline;
    private String primary_color;
    private String secondary_color;
    private String font;
    private LocalDateTime updated_at;
}
