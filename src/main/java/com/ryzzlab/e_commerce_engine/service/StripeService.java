package com.ryzzlab.e_commerce_engine.service;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import com.ryzzlab.e_commerce_engine.exception.AppException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripeService {
    @Value("${stripe.secret.key}")
    private String secretKey;
    @Value("${stripe.webhook.secret}")
    private String webhookSecret;
    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }
    public PaymentIntent createAndConfirmPaymentIntent(BigDecimal amount,String paymentMethodToken){
        Long amountInCents = amount.multiply(BigDecimal.valueOf(100)).longValueExact();
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency("eur")
                .setPaymentMethod(paymentMethodToken)
                .addPaymentMethodType("card")
                .setConfirm(true)
                .build();
        try{
            return PaymentIntent.create(params);
        }
        catch (StripeException e){
            throw new AppException("Payment failed: " + e.getMessage(),402);
        }
    }
    public Event constructWebhookEvent(String payload,String sigHeader){
        try {
            return Webhook.constructEvent(payload,sigHeader,webhookSecret);
        }
        catch (SignatureVerificationException e){
            throw new AppException("Invalid webhook signature", 400);
        }
    }
}
