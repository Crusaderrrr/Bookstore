package com.bookstore.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.app.model.User;
import com.bookstore.app.service.AuthService;
import com.bookstore.app.service.impl.MyUserDetailsServiceImpl;

@RestController
@RequestMapping("/users")
public class UserController {
  private final MyUserDetailsServiceImpl userDetailsService;
  private final AuthService authService;

  public UserController(MyUserDetailsServiceImpl userDetailsService, AuthService authService) {
    this.userDetailsService = userDetailsService;
    this.authService = authService;
  }

  @PostMapping("/login")
  public String login(@RequestBody User user) {
    return authService.verify(user);
  }

  @GetMapping("/all") 
  public List<User> getAllUsers() {
    return userDetailsService.getAllUsers();
  }
}
