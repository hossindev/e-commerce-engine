package com.ryzzlab.e_commerce_engine;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "shops")
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID shopId;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    private String subdomain;
    private String name;
    private String description;
    private String template_name;
    private LocalDateTime created_at;
}
