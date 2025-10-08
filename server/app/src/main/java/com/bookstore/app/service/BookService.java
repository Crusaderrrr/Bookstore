package com.bookstore.app.service;

import com.bookstore.app.model.Author;
import com.bookstore.app.model.Book;
import com.bookstore.app.model.BookImage;
import com.bookstore.app.repo.BookRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BookService {
  private final BookRepo bookRepo;
  private final BookImageService bookImageService;
  private final CloudinaryService cloudinaryService;

  public BookService(
      BookRepo bookRepo, BookImageService bookImageService, CloudinaryService cloudinaryService) {
    this.bookRepo = bookRepo;
    this.bookImageService = bookImageService;
    this.cloudinaryService = cloudinaryService;
  }

  public Book getBookById(Long id) throws EntityNotFoundException {
    return bookRepo
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
  }

  public Iterable<Book> getAllBooks() {
    return bookRepo.findAll();
  }

  @Transactional
  public void saveBook(Book book, Author author, MultipartFile file) throws IOException {
    if (file != null && !file.isEmpty()) {
      BookImage image = bookImageService.saveBookImage(file, book);
      book.setBookImage(image);
    }
    book.setAuthor(author);
    book.setDatePosted(LocalDate.now());
    bookRepo.save(book);
  }

  public List<Book> getBooksByAuthor(Author author) {
    return bookRepo.findByAuthor(author);
  }

  public List<Book> searchBooksByTitle(String keyword) {
    return bookRepo.findByTitleContainingIgnoreCase(keyword);
  }

  @Transactional
  public void deleteBooksById(List<Long> bookIds) throws IOException {
    List<Book> books = bookRepo.findAllById(bookIds);

    for (Book book : books) {
      if (book.getBookImage() != null) {
        cloudinaryService.deleteFile(book.getBookImage().getPublicId());
      }
    }
    bookRepo.deleteAll(books);
    bookRepo.flush();
  }
}
