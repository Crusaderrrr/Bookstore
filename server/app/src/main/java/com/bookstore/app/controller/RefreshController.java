package com.bookstore.app.controller;

import com.bookstore.app.model.User;
import com.bookstore.app.service.JWTService;
import com.bookstore.app.service.RefreshService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshController {
  private final RefreshService refreshService;
  private final JWTService jwtService;

  public RefreshController(RefreshService refreshService, JWTService jwtService) {
    this.refreshService = refreshService;
    this.jwtService = jwtService;
  }

  @PostMapping("/refresh_token")
  public ResponseEntity<?> refreshToken(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    String refreshToken = null;
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("refreshToken".equals(cookie.getName())) {
          refreshToken = cookie.getValue();
          break;
        }
      }
    }

    if (refreshToken == null) {
      throw new RuntimeException("Refresh token cookie not found");
    }

    try {
      refreshService.verifyExpiration(refreshService.getRefreshToken(refreshToken));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }

    User user = refreshService.getUserFromRefreshToken(refreshToken);
    String newAccessToken = jwtService.generateToken(user);

    Map<String, String> response = new HashMap<>();
    response.put("accessToken", newAccessToken);
    response.put("refreshToken", refreshToken);

    return ResponseEntity.ok(response);
  }
}
