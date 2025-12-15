package com.bookstore.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paypal")
public class WebController {

    @GetMapping("/")
    public String index() {
        return "payment";
    }

    @GetMapping("/success")
    public String success() {
        return "payment successful";
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "payment cancelled";
    }
}
