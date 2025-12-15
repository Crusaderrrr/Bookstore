package com.bookstore.app.controller;

import com.bookstore.app.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/paypal")
public class PaypalController {

    private static final String SUCCESS_URL = "http://localhost:8080/api/paypal/success";
    private static final String CANCEL_URL = "http://localhost:8080/api/paypal/cancel";

    @Autowired
    private PaypalService paypalService;

    @PostMapping("/create-payment")
    public String createPayment(@RequestParam double amount) throws PayPalRESTException {
        Payment payment = paypalService.createPayment(
                amount,
                "USD",
                "paypal",
                "sale",
                "payment description",
                CANCEL_URL,
                SUCCESS_URL
        );

        for (Links links : payment.getLinks()) {
            if (links.getRel().equals("approval_url")) {
                return "Redirect to :" + links.getHref();
            }
        }

        return "Error processing request";
    }

    @GetMapping("/success")
    public String paymentSuccess(@RequestParam("paymentId") String paymentId,
                                 @RequestParam("PayerID") String payerId)
            throws PayPalRESTException {

        Payment payment = paypalService.executePayment(paymentId, payerId);
        if (payment.getState().equals("approved")) {
            return "Success";
        }

        return "Error";

    }

    @GetMapping("/cancel")
    public String paymentCancel(@RequestParam("token") String token) {
        return "Payment cancelled.";
    }

}