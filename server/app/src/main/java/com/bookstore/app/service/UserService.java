package com.bookstore.app.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookstore.app.model.User;
import com.bookstore.app.repo.UserRepo;

@Service
public class UserService {
  private final UserRepo userRepo;
  private final PasswordEncoder pswEncoder;

  public UserService(UserRepo userRepo, PasswordEncoder pswEncoder) {
    this.userRepo = userRepo;
    this.pswEncoder = pswEncoder;
  }

  public User findUserByUsername(String username) {
    return userRepo.findUserByUsername(username).get();
  }

  public User saveUser(User user) {
    user.setPassword(pswEncoder.encode(user.getPassword()));
    return userRepo.save(user);
  }

  public Iterable<User> getAllUsers() {
    return userRepo.findAll();
  }
}
