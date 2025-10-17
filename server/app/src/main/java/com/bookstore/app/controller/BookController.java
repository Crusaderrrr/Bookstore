package com.bookstore.app.controller;

import com.bookstore.app.model.Author;
import com.bookstore.app.model.Book;
import com.bookstore.app.model.Genre;
import com.bookstore.app.service.AuthorService;
import com.bookstore.app.service.BookService;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        return bookService.getBookById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> getMethodName(
            @RequestParam(value = "q", required = true) String query,
            @RequestParam(value = "genre", required = false) Genre genre) {
        List<Book> books = null;
        if (query != null && genre != null) {
            books = bookService.findBookByTitleAndGenre(query, genre);
        } else if (query != null && genre == null) {
            books = bookService.findBooksByTitle(query);
        }
        return ResponseEntity.ok(books);
    }

    @PostMapping("/add-books")
    public ResponseEntity<String> addBooks(@RequestBody List<Book> books, Principal principal)
            throws IOException {
        Author author = authorService.getAuthorByUsername(principal.getName());
        for (Book book : books) {
            bookService.saveBook(book, author, null);
        }
        return ResponseEntity.ok("Books added");
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR')")
    public ResponseEntity<String> deleteBooks(@RequestBody List<Long> bookIds) throws IOException {
        bookService.deleteBooksById(bookIds);
        return ResponseEntity.ok("Books deleted");
    }
}
