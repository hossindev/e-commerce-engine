package com.ryzzlab.e_commerce_engine.repository;

import com.ryzzlab.e_commerce_engine.entity.Address;
import com.ryzzlab.e_commerce_engine.entity.ShopCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    List<Address> findAllByShopCustomer(ShopCustomer customer);
    Optional<Address> findByAddressIdAndShopCustomer(UUID addressId,ShopCustomer customer);
}
