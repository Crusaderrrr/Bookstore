package com.bookstore.app.repo;

import com.bookstore.app.model.Author;
import com.bookstore.app.model.ModerationRequest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModerationRequestRepo extends JpaRepository<ModerationRequest, Long> {
  List<ModerationRequest> findByAuthor(Author author);
}
