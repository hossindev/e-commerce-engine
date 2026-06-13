package com.ryzzlab.e_commerce_engine.service;

import com.ryzzlab.e_commerce_engine.dto.ShopResponse;
import com.ryzzlab.e_commerce_engine.entity.Shop;
import com.ryzzlab.e_commerce_engine.entity.ShopCustomization;
import com.ryzzlab.e_commerce_engine.entity.User;
import com.ryzzlab.e_commerce_engine.exception.AppException;
import com.ryzzlab.e_commerce_engine.repository.ShopCustomizationRepository;
import com.ryzzlab.e_commerce_engine.repository.ShopRepository;
import com.ryzzlab.e_commerce_engine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ShopService {
    private ShopResponse mapToResponse(Shop shop, ShopCustomization shopCustomization) {
        ShopResponse response = new ShopResponse();
        response.setShopId(shop.getShopId());
        response.setName(shop.getName());
        response.setSubdomain(shop.getSubdomain());
        response.setDescription(shop.getDescription());
        response.setTemplateName(shop.getTemplateName());
        response.setLogoUrl(shopCustomization.getLogoUrl());
        response.setBannerUrl(shopCustomization.getBannerUrl());
        response.setTagline(shopCustomization.getTagline());
        response.setPrimaryColor(shopCustomization.getPrimaryColor());
        response.setSecondaryColor(shopCustomization.getSecondaryColor());
        response.setFont(shopCustomization.getFont());
        return response;
    }
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ShopCustomizationRepository shopCustomizationRepository;
    @Autowired
    private UserRepository userRepository;
    public ShopResponse createShop(UUID userId,String name,String subdomain,String templateName,String description){
        User user = userRepository.findByUserId(userId).orElseThrow(()-> new AppException("User not found",404));

        Optional<Shop> optionalSubdomain = shopRepository.findBySubdomain(subdomain);
        if(optionalSubdomain.isPresent()){
            throw new AppException("A shop with this subdomain already exists", 409);
        }
        Optional<Shop> existingShop = shopRepository.findByUser(user);
        if(existingShop.isPresent()){
            throw new AppException("Shop already exists", 409);
        }
        Shop shop = new Shop();
        shop.setUser(user);
        shop.setName(name);
        shop.setDescription(description);
        shop.setSubdomain(subdomain);
        shop.setTemplateName(templateName);
        shopRepository.save(shop);
        ShopCustomization shopCustomization = new ShopCustomization();
        shopCustomization.setShop(shop);
        shopCustomizationRepository.save(shopCustomization);

        return mapToResponse(shop,shopCustomization);
    }

    public ShopResponse updateCustomization(UUID shopId, UUID userId, String logoUrl, String bannerUrl, String tagline, String primaryColor, String secondaryColor, String font){
        Shop shop = shopRepository.findById(shopId).orElseThrow(()-> new AppException("No shop found",404));
        if(!shop.getUser().getUserId().equals(userId)) throw new AppException("Forbidden", 403);
        ShopCustomization shopCustomization = shopCustomizationRepository.findByShop(shop).orElseThrow();
        if(logoUrl != null) shopCustomization.setLogoUrl(logoUrl);
        if(bannerUrl != null) shopCustomization.setBannerUrl(bannerUrl);
        if(tagline != null) shopCustomization.setTagline(tagline);
        if(primaryColor != null) shopCustomization.setPrimaryColor(primaryColor);
        if(secondaryColor != null) shopCustomization.setSecondaryColor(secondaryColor);
        if(font != null) shopCustomization.setFont(font);
        shopCustomizationRepository.save(shopCustomization);
        return mapToResponse(shop, shopCustomization);
    }
    public ShopResponse getShopBySubdomain(String subdomain){
        Shop shop = shopRepository.findBySubdomain(subdomain).orElseThrow(()-> new AppException("Shop not found",404));
        ShopCustomization shopCustomization = shopCustomizationRepository.findByShop(shop).orElseThrow();
        return mapToResponse(shop,shopCustomization);
    }
}
