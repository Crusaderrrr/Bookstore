package com.bookstore.app.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bookstore.app.model.AuthResponse;
import com.bookstore.app.model.RefreshToken;
import com.bookstore.app.model.User;

@Service
public class AuthService {
    private final AuthenticationManager authManager;
    private final JWTService jwtService;
    private final RefreshService refreshService;


    public AuthService(AuthenticationManager authManager, JWTService jwtService, RefreshService refreshService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.refreshService = refreshService;
    }

    public AuthResponse verify(User user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            String accessToken = jwtService.generateToken(user.getUsername());
            RefreshToken refToken = refreshService.createRefreshToken(user.getUsername());
            return new AuthResponse(accessToken, refToken.getToken());
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }
}