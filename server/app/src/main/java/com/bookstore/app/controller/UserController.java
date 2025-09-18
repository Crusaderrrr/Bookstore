package com.bookstore.app.controller;

import com.bookstore.app.dto.UserDTO;
import com.bookstore.app.model.AuthResponse;
import com.bookstore.app.model.User;
import com.bookstore.app.service.AuthService;
import com.bookstore.app.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;
  private final AuthService authService;

  @Value("${app.jwtRefreshExpirationMs}")
  private Long refreshTokenDurationMs;

  public UserController(UserService userService, AuthService authService) {
    this.userService = userService;
    this.authService = authService;
  }

  @PostMapping("/login")
  public String login(@RequestBody User user, HttpServletResponse response) {
    AuthResponse authResponse = authService.verify(user);
    Cookie refreshCookie = new Cookie("refreshToken", authResponse.getRefreshToken());
    refreshCookie.setHttpOnly(true);
    refreshCookie.setSecure(true);
    refreshCookie.setPath("/");
    refreshCookie.setMaxAge(refreshTokenDurationMs.intValue() / 1000);
    response.addCookie(refreshCookie);
    return authResponse.getAccessToken();
  }

  @GetMapping("/all")
  @PreAuthorize("isAuthenticated()")
  public Iterable<User> getAllUsers() {
    return userService.getAllUsers();
  }

  @PostMapping("/new")
  public ResponseEntity<String> newUser(@Valid @RequestBody UserDTO user) {
    userService.saveUser(user);
    return ResponseEntity.ok("User created");
  }
}
