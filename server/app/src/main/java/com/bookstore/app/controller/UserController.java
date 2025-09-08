package com.bookstore.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bookstore.app.model.User;
import com.bookstore.app.repo.UserRepo;
import com.bookstore.app.service.impl.MyUserDetailsServiceImpl;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserRepo userRepo;
  private final PasswordEncoder passwordEncoder;
  private final MyUserDetailsServiceImpl userDetailsService;


  public UserController(UserRepo userRepo, PasswordEncoder passwordEncoder, MyUserDetailsServiceImpl userDetailsService) {
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
    this.userDetailsService = userDetailsService;
  }

  @GetMapping
  public List<User> getUsers() {
    return userRepo.findAll();
  }
  
  @PostMapping("/register")
  public void createUser(@RequestBody User user) {
    userDetailsService.createUser(user);
  }

  @PutMapping("/update/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public User updateUser(@PathVariable int id, @RequestBody User user) {
    return userRepo.findById(id).map(existingUser -> {
      existingUser.setUsername(user.getUsername());
      existingUser.setEmail(user.getEmail());
      if (user.getPassword() != null && !user.getPassword().isEmpty()) {
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
      }
      existingUser.setRoles(user.getRoles());
      existingUser.setActive(user.isActive());
      return userRepo.save(existingUser);
    }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found"));
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<String> deleteUser(@PathVariable int id) {
    if (!userRepo.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found");
    }
    userRepo.deleteById(id);
    return ResponseEntity.ok("User with id " + id + " has been deleted successfully.");
  }
}
