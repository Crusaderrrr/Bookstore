package com.bookstore.app.repo;

import com.bookstore.app.model.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserUsername(String username);
}
