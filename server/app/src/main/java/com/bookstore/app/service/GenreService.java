package com.bookstore.app.service;

import com.bookstore.app.model.Genre;
import com.bookstore.app.repo.GenreRepo;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GenreService {
    private final GenreRepo genreRepo;

    public GenreService(GenreRepo genreRepo) {
        this.genreRepo = genreRepo;
    }

    public List<Genre> getAllGenres() {
        return genreRepo.findAll();
    }

    public void addGenre(Genre genre) {
        genreRepo.save(genre);
    }
}
