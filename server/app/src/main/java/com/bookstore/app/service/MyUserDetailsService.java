package com.bookstore.app.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.bookstore.app.model.User;

public interface MyUserDetailsService extends UserDetailsService {
  User createUser(User user);
}
