package com.ryzzlab.e_commerce_engine.service;

import com.ryzzlab.e_commerce_engine.dto.ProductResponse;
import com.ryzzlab.e_commerce_engine.entity.Product;
import com.ryzzlab.e_commerce_engine.entity.Shop;
import com.ryzzlab.e_commerce_engine.entity.User;
import com.ryzzlab.e_commerce_engine.exception.AppException;
import com.ryzzlab.e_commerce_engine.repository.ProductRepository;
import com.ryzzlab.e_commerce_engine.repository.ShopRepository;
import com.ryzzlab.e_commerce_engine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private UserRepository userRepository;
    private ProductResponse mapToResponse(Product product){
        ProductResponse response = new ProductResponse();
        response.setProductId(product.getProductId());
        response.setSlug(product.getSlug());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setImageUrl(product.getImageUrl());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setSold(product.getSold());

        return response;
    }
    public ProductResponse createProduct(UUID userId, UUID shopId, String name, String description, String imageUrl, BigDecimal price,Integer stockQuantity){
        Shop shop = shopRepository.findById(shopId).orElseThrow(()-> new AppException("No shop found",404));
        if(!shop.getUser().getUserId().equals(userId)) throw new AppException("Forbidden", 403);
        String baseSlug = name.toLowerCase().trim().replaceAll("\\s+", "-");
        String slug = baseSlug;
        int count = 1;
        while(productRepository.findByShopAndSlug(shop, slug).isPresent()) {
            slug = baseSlug + "-" + count;
            count++;
        }
        Product product = new Product();
        product.setSlug(slug);
        product.setPrice(price);
        product.setSold(0);
        product.setImageUrl(imageUrl);
        product.setName(name);
        product.setDescription(description);
        product.setStockQuantity(stockQuantity);
        product.setShop(shop);
        productRepository.save(product);
        return mapToResponse(product);
    }

    public ProductResponse updateProduct(UUID userId,UUID productId,String name,String description,String imageUrl,BigDecimal price,Integer stockQuantity){
        Product product = productRepository.findById(productId).orElseThrow(()-> new AppException("Product not found",404));
        if(!product.getShop().getUser().getUserId().equals(userId)) throw new AppException("Forbidden", 403);
        if(name != null) product.setName(name);
        if(description != null) product.setDescription(description);
        if(imageUrl != null) product.setImageUrl(imageUrl);
        if(price != null) product.setPrice(price);
        if(stockQuantity != null) product.setStockQuantity(stockQuantity);
        productRepository.save(product);
        return mapToResponse(product);
    }

    public void deleteProduct(UUID userId, UUID productId){
        Product product = productRepository.findById(productId).orElseThrow(()-> new AppException("Product not found",404));
        if(!product.getShop().getUser().getUserId().equals(userId)) throw new AppException("Forbidden", 403);
        productRepository.delete(product);
    }
    public List<ProductResponse> getShopProducts(String subdomain){
        Shop shop = shopRepository.findBySubdomain(subdomain).orElseThrow(()-> new AppException("Shop not found",404));
        List<Product> products = productRepository.findAllProductsByShop(shop);
        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }
}
