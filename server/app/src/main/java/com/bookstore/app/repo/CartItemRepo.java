package com.bookstore.app.repo;

import com.bookstore.app.model.CartItem;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {
    @Modifying
    @Transactional
    void deleteByBookIdIn(List<Long> ids);
}
