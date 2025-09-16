package com.bookstore.app.service;

import com.bookstore.app.model.RefreshToken;
import com.bookstore.app.model.User;
import com.bookstore.app.repo.RefreshTokenRepository;
import com.bookstore.app.repo.UserRepo;
import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RefreshService {
  @Value("${app.jwtRefreshExpirationMs}")
  private long refreshTokenDurationMs;

  @Autowired private RefreshTokenRepository refreshTokenRepository;

  @Autowired private UserRepo userRepository;

  public RefreshService() { }

  @Transactional
  public RefreshToken createRefreshToken(String username) {
    User user =
        userRepository
            .findUserByUsername(username)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found with name " + username));

    refreshTokenRepository.deleteByUser(user);
    refreshTokenRepository.flush();

    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUser(user);
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
    refreshToken.setToken(UUID.randomUUID().toString());

    return refreshTokenRepository.save(refreshToken);
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Refresh token was expired. Please make a new signin request");
    }
    return token;
  }

  @Transactional
  public void delete(String username) {
    User user = userRepository.findUserByUsername(username).get();
    refreshTokenRepository.deleteByUser(user);
  }
}
