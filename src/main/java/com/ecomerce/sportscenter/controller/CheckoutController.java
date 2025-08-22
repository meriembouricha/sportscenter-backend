package com.ecomerce.sportscenter.controller;

import com.ecomerce.sportscenter.Security.JwtHelper;
import com.ecomerce.sportscenter.model.CheckoutRequest;
import com.ecomerce.sportscenter.service.CheckoutService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = "http://localhost:4200")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    JwtHelper jwtHelper;

//    @PostMapping("/session")
//    public ResponseEntity<String> createCheckoutSession(@RequestBody CheckoutRequest checkoutRequest) {
//        try {
//            String sessionUrl = checkoutService.createCheckoutSession(checkoutRequest);
//            return ResponseEntity.ok(sessionUrl);
//        } catch (StripeException e) {
//            return ResponseEntity.badRequest().body("Erreur Stripe: " + e.getMessage());
//        }
//    }
@PostMapping("/session")
public ResponseEntity<String> createCheckoutSession(@RequestHeader("Authorization") String tokenHeader) {
    try {
        String token = jwtHelper.extractTokenFromHeader(tokenHeader);
        Long userId = jwtHelper.getUserIdFromToken(token);

        String sessionUrl = checkoutService.createCheckoutSession(userId);
        return ResponseEntity.ok(sessionUrl);
    } catch (StripeException e) {
        return ResponseEntity.badRequest().body("Erreur Stripe: " + e.getMessage());
    }
}

}

