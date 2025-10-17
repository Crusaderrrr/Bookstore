package com.bookstore.app.service;

import com.bookstore.app.model.Author;
import com.bookstore.app.model.User;
import com.bookstore.app.repo.AuthorRepo;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    private final AuthorRepo authorRepo;
    private final UserService userService;

    public AuthorService(AuthorRepo authorRepo, UserService userService) {
        this.authorRepo = authorRepo;
        this.userService = userService;
    }

    public void createAuthor(Author author, String username) {
        User user = userService.findByUsername(username);
        if (!user.getRoles().equals("ROLE_ADMIN")) {
            user.setRoles("ROLE_AUTHOR");
        }
        author.setUser(user);
        authorRepo.save(author);
    }

    public Author getAuthorByUsername(String username) {
        User user = userService.findByUsername(username);
        return authorRepo.findByUserId(user.getId()).get();
    }

    public Optional<Author> getAuthorOptByUsername(String username) {
        User user = userService.findByUsername(username);
        return authorRepo.findByUserId(user.getId());
    }

    public Optional<Author> getAuthorByAuthorName(String authorName) {
        return authorRepo.findByName(authorName);
    }
}
