package com.bookstore.app.repo;

import com.bookstore.app.model.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookImageRepo extends JpaRepository<BookImage, Long> { }
