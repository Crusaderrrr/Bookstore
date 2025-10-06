package com.bookstore.app.repo;

import com.bookstore.app.model.Author;
import com.bookstore.app.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book, Long> {
  List<Book> findByAuthor(Author author);

  List<Book> findByTitleContainingIgnoreCase(String title);
}
