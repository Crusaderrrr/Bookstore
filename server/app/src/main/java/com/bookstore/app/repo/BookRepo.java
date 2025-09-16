package com.bookstore.app.repo;

import com.bookstore.app.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book, Long> { }
