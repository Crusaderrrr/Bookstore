package com.bookstore.app.service;

import com.bookstore.app.exception.InvalidCredentialsException;
import com.bookstore.app.model.AuthResponse;
import com.bookstore.app.model.RefreshToken;
import com.bookstore.app.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final AuthenticationManager authManager;
  private final JWTService jwtService;
  private final RefreshService refreshService;
  private final UserService userService;

  public AuthService(
      AuthenticationManager authManager,
      JWTService jwtService,
      RefreshService refreshService,
      UserService userService) {
    this.authManager = authManager;
    this.jwtService = jwtService;
    this.refreshService = refreshService;
    this.userService = userService;
  }

  public AuthResponse verify(User user) {
    try {
      Authentication authentication =
          authManager.authenticate(
              new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

      User authUser = userService.findByUsername(user.getUsername());
      String accessToken = jwtService.generateToken(authUser);
      RefreshToken refToken = refreshService.createRefreshToken(user.getUsername());
      return new AuthResponse(accessToken, refToken.getToken());
    } catch (BadCredentialsException ex) {
      throw new InvalidCredentialsException("Invalid username or password");
    }
  }
}
