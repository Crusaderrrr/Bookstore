package com.bookstore.app.repo;

import com.bookstore.app.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem, Long> { }
