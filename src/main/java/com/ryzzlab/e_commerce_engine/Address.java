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
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID addressId;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    private String street;
    private String city;
    private String postal_code;
    private String country ;
    private boolean is_default;
}
