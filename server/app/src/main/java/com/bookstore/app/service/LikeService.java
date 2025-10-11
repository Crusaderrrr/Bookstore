package com.bookstore.app.service;

import com.bookstore.app.model.Book;
import com.bookstore.app.model.Like;
import com.bookstore.app.model.User;
import com.bookstore.app.repo.LikeRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {
  private final LikeRepo likeRepo;
  private final BookService bookService;
  private final UserService userService;

  public LikeService(LikeRepo likeRepo, BookService bookService, UserService userService) {
    this.likeRepo = likeRepo;
    this.bookService = bookService;
    this.userService = userService;
  }

  public Like getLikeByBookId(long bookId) {
    return likeRepo.findLikeByBookId(bookId).get();
  }

  @Transactional
  public void addLikeForBook(Long bookId, String username) {
    Book book = bookService.getBookById(bookId);
    User user = userService.findByUsername(username);
    Like like = new Like();
    like.setBook(book);
    like.setUser(user);
    likeRepo.save(like);
  }

  public void removeLikeForBook(long bookId) {
    Like like = getLikeByBookId(bookId);
    likeRepo.delete(like);
  }
}
