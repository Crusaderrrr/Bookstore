package com.bookstore.app.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.app.model.RefreshToken;
import com.bookstore.app.model.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
