package com.bookstore.app.service;

import com.bookstore.app.model.Author;
import com.bookstore.app.model.Book;
import com.bookstore.app.repo.BookRepo;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BookService {
  private final BookRepo bookRepo;

  public BookService(BookRepo bookRepo) {
    this.bookRepo = bookRepo;
  }

  public Optional<Book> getBookById(Long id) {
    return bookRepo.findById(id);
  }

  public Iterable<Book> getAllBooks() {
    return bookRepo.findAll();
  }

  public void saveBook(Book book, Author author) {
    book.setAuthor(author);
    book.setDatePosted(LocalDate.now());
    bookRepo.save(book);
  }
}
