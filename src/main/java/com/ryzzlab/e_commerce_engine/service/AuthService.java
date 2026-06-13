package com.ryzzlab.e_commerce_engine.service;

import com.ryzzlab.e_commerce_engine.exception.AppException;
import com.ryzzlab.e_commerce_engine.security.JwtUtil;
import com.ryzzlab.e_commerce_engine.entity.Shop;
import com.ryzzlab.e_commerce_engine.entity.ShopCustomer;
import com.ryzzlab.e_commerce_engine.entity.User;
import com.ryzzlab.e_commerce_engine.repository.ShopCustomerRepository;
import com.ryzzlab.e_commerce_engine.repository.ShopRepository;
import com.ryzzlab.e_commerce_engine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ShopCustomerRepository shopCustomerRepository;
    @Autowired
    private ShopRepository shopRepository;
    public String  registerBuyer(String email,String password, String fullName,String subdomain){

        Shop shop = shopRepository.findBySubdomain(subdomain).orElseThrow();

        if(shopCustomerRepository.findByEmailAndShop(email,shop).isPresent()){
            throw new AppException("User already has an account", 409);
        }
        ShopCustomer user = new ShopCustomer();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setShop(shop);
        shopCustomerRepository.save(user);
        String token = jwtUtil.generateToken(user.getId().toString());
        return token;
    }
    public String  loginBuyer(String email,String password, String subdomain){
        Shop shop = shopRepository.findBySubdomain(subdomain)
                .orElseThrow(() -> new AppException("Shop not found", 404));
        ShopCustomer shopCustomer = shopCustomerRepository.findByEmailAndShop(email, shop)
                .orElseThrow(() -> new AppException("Invalid credentials", 401));
        if(!passwordEncoder.matches(password, shopCustomer.getPasswordHash())){
            throw new AppException("Invalid credentials", 401);
        }
        String token = jwtUtil.generateToken(shopCustomer.getId().toString());
        return token;
    }

    public String registerOwner(String email,String password, String fullName){
        if(userRepository.findByEmail(email).isPresent()){
            throw new AppException("User already has an account", 409);
        }
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setFullName(fullName);

        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getUserId().toString());
        return token;
    }
    public String  loginOwner(String email,String password){

        User user = userRepository.findByEmail(email).orElseThrow();
        if(!passwordEncoder.matches(password, user.getPasswordHash())){
            throw new AppException("Invalid credentials", 401);
        }
        String token = jwtUtil.generateToken(user.getUserId().toString());
        return token;
    }

}
