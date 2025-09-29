package com.bookstore.app.controller;

import com.bookstore.app.model.Author;
import com.bookstore.app.model.Book;
import com.bookstore.app.service.AuthorService;
import com.bookstore.app.service.BookService;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {
  private final BookService bookService;
  private final AuthorService authorService;

  public BookController(BookService bookService, AuthorService authorService) {
    this.bookService = bookService;
    this.authorService = authorService;
  }

  @GetMapping("/all")
  public Iterable<Book> getAllBooks() {
    return bookService.getAllBooks();
  }

  @GetMapping("/{id}")
  public Book getBookById(@PathVariable Long id) {
    return bookService.getBookById(id).get();
  }

  @PostMapping("/new")
  public ResponseEntity<String> newBook(@Valid @RequestBody Book book, Principal principal) {
    Author author = authorService.getAuthorByUsername(principal.getName());
    bookService.saveBook(book, author);
    return ResponseEntity.ok("Book created");
  }
}
