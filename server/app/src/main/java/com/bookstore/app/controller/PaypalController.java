package com.bookstore.app.controller;

import com.bookstore.app.service.PaypalService;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/paypal")
public class PaypalController {
    @Autowired
    private PaypalService paypalService;

    @PostMapping("/complete")
    public ResponseEntity<?> completePayment(@RequestBody Map<String, String> payload) {
        String orderId = payload.get("orderID");
        try {
            boolean isCaptured = paypalService.captureOrder(orderId);
            if (isCaptured) {
                // Save order as paid in your DB here
                return ResponseEntity.ok(Collections.singletonMap("success", true));
            } else {
                return ResponseEntity.ok(Collections.singletonMap("success", false));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("success", false));
        }
    }
}