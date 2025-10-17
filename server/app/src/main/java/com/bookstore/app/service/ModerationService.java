package com.bookstore.app.service;

import com.bookstore.app.dto.ModerationRequestDTO;
import com.bookstore.app.model.Author;
import com.bookstore.app.model.Book;
import com.bookstore.app.model.BookImage;
import com.bookstore.app.model.Genre;
import com.bookstore.app.model.ModerationRequest;
import com.bookstore.app.model.ModerationStatus;
import com.bookstore.app.repo.ModerationRequestRepo;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ModerationService {
    private final ModerationRequestRepo moderationRequestRepo;
    private final BookService bookService;
    private final AuthorService authorService;
    private final BookImageService bookImageService;
    private final CloudinaryService cloudinaryService;

    public ModerationService(ModerationRequestRepo moderationRequestRepo, BookService bookService,
            AuthorService authorService, BookImageService bookImageService,
            CloudinaryService cloudinaryService) {
        this.moderationRequestRepo = moderationRequestRepo;
        this.bookService = bookService;
        this.authorService = authorService;
        this.bookImageService = bookImageService;
        this.cloudinaryService = cloudinaryService;
    }

    public void saveModerationRequest(ModerationRequest request) {
        moderationRequestRepo.save(request);
    }

    public List<ModerationRequest> findAll() {
        return moderationRequestRepo.findAll();
    }

    public void approveModerationRequest(Long id) {
        ModerationRequest request = moderationRequestRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Moderation request not found"));
        request.setStatus(ModerationStatus.APPROVED);
        Author author = authorService.getAuthorByAuthorName(request.getAuthor().getName())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Author with name " + request.getAuthor().getName() + " not found"));
        BookImage bookImage = new BookImage();
        bookImage.setPublicId(request.getImagePublicId());
        bookImage.setUrl(request.getImageUrl());

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setDescription(request.getDescription());
        book.setPrice(request.getPrice());
        book.setGenre(request.getGenre());

        bookService.saveBook(book, author, bookImage);
        moderationRequestRepo.save(request);
    }

    public void rejectModerationRequest(Long id, String reason) {
        ModerationRequest request = moderationRequestRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Moderation request not found"));
        request.setStatus(ModerationStatus.REJECTED);
        try {
            cloudinaryService.deleteFile(request.getImagePublicId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        request.setReason(reason);
        moderationRequestRepo.save(request);
    }

    public void deleteRequest(Long id) {
        moderationRequestRepo.deleteById(id);
    }

    public List<ModerationRequestDTO> finAllRequestsByUsername(String username) {
        Author author = authorService.getAuthorByUsername(username);
        List<ModerationRequest> requests = moderationRequestRepo.findByAuthor(author);
        return requests.stream().map(ModerationRequest::toDTO).toList();
    }
}
