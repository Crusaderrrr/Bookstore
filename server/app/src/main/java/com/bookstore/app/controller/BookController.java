package com.bookstore.app.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.app.model.Book;
import com.bookstore.app.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {
  private final BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping("/all")
  public Iterable<Book> getAllBooks() {
    return bookService.getAllBooks();
  }

  @PostMapping("/new")
  public ResponseEntity<String> newBook(@RequestBody Book book, Principal principal) {
    bookService.saveBook(book, principal.getName());
    return ResponseEntity.ok("Book created");
  }

}
