package com.ryzzlab.e_commerce_engine.controller;

import com.ryzzlab.e_commerce_engine.entity.Order;
import com.ryzzlab.e_commerce_engine.entity.Status;
import com.ryzzlab.e_commerce_engine.exception.AppException;
import com.ryzzlab.e_commerce_engine.repository.OrderRepository;
import com.ryzzlab.e_commerce_engine.service.StripeService;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {
    @Autowired
    private StripeService stripeService;
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/stripe")
    public ResponseEntity<?> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ){
        try {
            Event event = stripeService.constructWebhookEvent(payload, sigHeader);

            if ("payment_intent.succeeded".equals(event.getType())) {
                Optional<StripeObject> dataObject = event.getDataObjectDeserializer().getObject();
                if (dataObject.isPresent() && dataObject.get() instanceof PaymentIntent paymentIntent) {
                    orderRepository.findByStripePaymentIntentId(paymentIntent.getId())
                            .ifPresent(order -> {
                                if (order.getStatus() != Status.PAID) {
                                    order.setStatus(Status.PAID);
                                    orderRepository.save(order);
                                }
                            });
                }
            }
            return ResponseEntity.ok().build();
        } catch (AppException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}
