package com.bookstore.app.service.impl;

import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookstore.app.model.MyUserDetails;
import com.bookstore.app.model.User;
import com.bookstore.app.repo.UserRepo;
import com.bookstore.app.service.JWTService;
import com.bookstore.app.service.MyUserDetailsService;

@Service
public class MyUserDetailsServiceImpl implements MyUserDetailsService{
  
  private final UserRepo userRepo;
  private final PasswordEncoder passwordEncoder;

  public MyUserDetailsServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, JWTService jwtService) {
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepo.findUserByUsername(username);
    System.out.println("Searching for user by username");
    
    return user.map(MyUserDetails::new).orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
  }

}