package com.bookstore.app.repo;

import com.bookstore.app.model.Author;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepo extends JpaRepository<Author, Long> {
  Optional<Author> findByUserId(int userId);

  Optional<Author> findByName(String name);
}
