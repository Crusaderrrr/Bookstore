package com.bookstore.app.service;

import com.bookstore.app.model.Book;
import com.bookstore.app.model.BookImage;
import com.bookstore.app.repo.BookImageRepo;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BookImageService {
  private final BookImageRepo bookImageRepo;
  private final CloudinaryService cloudinaryService;

  public BookImageService(BookImageRepo bookImageRepo, CloudinaryService cloudinaryService) {
    this.bookImageRepo = bookImageRepo;
    this.cloudinaryService = cloudinaryService;
  }

  public BookImage saveBookImage(MultipartFile file, Book book) throws IOException {
    Map uploadResult = cloudinaryService.uploadFile(file);

    String publicId = (String) uploadResult.get("public_id");
    String url = (String) uploadResult.get("secure_url");

    BookImage bookImage = new BookImage();
    bookImage.setPublicId(publicId);
    bookImage.setUrl(url);

    bookImage.setBook(book);
    book.setBookImage(bookImage);

    return bookImageRepo.save(bookImage);
  }
}
