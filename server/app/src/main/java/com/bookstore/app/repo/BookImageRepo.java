package com.bookstore.app.repo;

import com.bookstore.app.model.BookImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookImageRepo extends JpaRepository<BookImage, Long> {
    Optional<BookImage> findByBookId(Long bookId);

    void deleteByBookId(Long bookId);
}
