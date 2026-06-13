package com.ryzzlab.e_commerce_engine;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShopRepository extends JpaRepository<Shop, UUID> {

    Optional<Shop> findBySubdomain(String subdomain);
}
