package com.bookstore.app.repo;

import com.bookstore.app.model.Like;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepo extends JpaRepository<Like, Long> {
  Optional<Like> findLikeByBookId(long bookId);
}
