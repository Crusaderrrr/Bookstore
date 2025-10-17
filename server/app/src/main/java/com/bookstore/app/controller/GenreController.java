package com.bookstore.app.controller;

import com.bookstore.app.model.Genre;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/genres")
public class GenreController {

    @GetMapping
    public List<String> getAllGenres() {
        return Arrays.stream(Genre.values()).map(Enum::name).collect(Collectors.toList());
    }
}
