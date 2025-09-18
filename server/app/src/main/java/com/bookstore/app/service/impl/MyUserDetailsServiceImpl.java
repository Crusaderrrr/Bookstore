package com.bookstore.app.service.impl;

import com.bookstore.app.model.MyUserDetails;
import com.bookstore.app.model.User;
import com.bookstore.app.repo.UserRepo;
import com.bookstore.app.service.MyUserDetailsService;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsServiceImpl implements MyUserDetailsService {

  private final UserRepo userRepo;

  public MyUserDetailsServiceImpl(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepo.findUserByUsername(username);

    return user.map(MyUserDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
  }

  public List<User> getAllUsers() {
    return userRepo.findAll();
  }
}
