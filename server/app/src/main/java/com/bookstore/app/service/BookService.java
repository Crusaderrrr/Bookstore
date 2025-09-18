package com.bookstore.app.service;

import com.bookstore.app.model.Book;
import com.bookstore.app.model.User;
import com.bookstore.app.repo.BookRepo;
import org.springframework.stereotype.Service;

@Service
public class BookService {
  private final BookRepo bookRepo;
  private final UserService userService;

  public BookService(BookRepo bookRepo, UserService userService) {
    this.bookRepo = bookRepo;
    this.userService = userService;
  }

  public Book saveBook(Book book, String username) {
    User author = userService.findByUsername(username);
    book.setAuthor(author);
    return bookRepo.save(book);
  }

  public Book getBookById(Long id) {
    return bookRepo.findById(id).orElse(null);
  }

  public Iterable<Book> getAllBooks() {
    return bookRepo.findAll();
  }
}
