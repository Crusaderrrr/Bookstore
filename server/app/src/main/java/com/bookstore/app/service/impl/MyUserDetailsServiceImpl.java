package com.bookstore.app.service.impl;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bookstore.app.model.MyUserDetails;
import com.bookstore.app.model.User;
import com.bookstore.app.repo.UserRepo;
import com.bookstore.app.service.MyUserDetailsService;

@Service
public class MyUserDetailsServiceImpl implements MyUserDetailsService{
  
  private final UserRepo userRepo;
  private final PasswordEncoder passwordEncoder;


  public MyUserDetailsServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public MyUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<User> user = userRepo.findByEmail(email);
    
    return user.map(MyUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
  }

  @Override
  public User createUser(User user) {
    if (userRepo.findByEmail(user.getEmail()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email " + user.getEmail() + " is already in use.");
    }
    if (user.getRoles() == null || user.getRoles().isEmpty()) {
      user.setRoles("USER");
    }
    user.setActive(true);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepo.save(user);
  }
}
