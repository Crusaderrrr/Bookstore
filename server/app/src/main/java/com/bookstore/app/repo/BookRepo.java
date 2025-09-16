package com.bookstore.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.app.model.Book;

public interface BookRepo extends JpaRepository<Book, Long>{
  
}
