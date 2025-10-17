package com.bookstore.app.controller;

import com.bookstore.app.model.Author;
import com.bookstore.app.service.AuthorService;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/new")
    public ResponseEntity<String> createAuthor(@Valid @RequestBody Author author,
            Principal principal) {
        authorService.createAuthor(author, principal.getName());
        return ResponseEntity.ok("Author created");
    }
}
