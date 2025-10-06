package com.bookstore.app.controller;

import com.bookstore.app.model.Author;
import com.bookstore.app.model.Book;
import com.bookstore.app.service.AuthorService;
import com.bookstore.app.service.BookService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    return bookService.getBookById(id);
  }

  @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> newBook(
      @RequestPart("book") @Valid Book book,
      @RequestPart("file") MultipartFile file,
      Principal principal)
      throws IOException {

    Author author = authorService.getAuthorByUsername(principal.getName());
    bookService.saveBook(book, author, file);
    return ResponseEntity.ok("Book created");
  }

  @GetMapping("/search")
  public ResponseEntity<List<Book>> getMethodName(@RequestParam("q") String query) {
    List<Book> books = bookService.searchBooksByTitle(query);
    return ResponseEntity.ok(books);
  }
}
