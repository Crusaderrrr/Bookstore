package com.bookstore.app.service;

import com.bookstore.app.model.Book;
import com.bookstore.app.model.Cart;
import com.bookstore.app.model.CartItem;
import com.bookstore.app.model.User;
import com.bookstore.app.repo.CartItemRepo;
import com.bookstore.app.repo.CartRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CartService {
  private final BookService bookService;
  private final CartRepo cartRepo;
  private final UserService userService;
  private final CartItemRepo cartItemRepo;

  public CartService(
      BookService bookService,
      CartRepo cartRepo,
      UserService userService,
      CartItemRepo cartItemRepo) {
    this.bookService = bookService;
    this.cartRepo = cartRepo;
    this.userService = userService;
    this.cartItemRepo = cartItemRepo;
  }

  public Cart getCartByUsername(String username) {
    return cartRepo.findByUser_Username(username).orElseGet(() -> createCartForUser(username));
  }

  public Cart createCartForUser(String username) {
    User user =
        userService
            .findByUsername(username);

    Cart cart = new Cart();
    cart.setUser(user);
    return cartRepo.save(cart);
  }

  public Cart addToCart(String username, Long bookId, int quantity) {
    Cart cart = getCartByUsername(username);
    Book book =
        bookService.getBookById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

    Optional<CartItem> existingItem =
        cart.getItems().stream().filter(item -> item.getBook().getId().equals(bookId)).findFirst();

    if (existingItem.isPresent()) {
      CartItem item = existingItem.get();
      item.setQuantity(item.getQuantity() + quantity);
      cartItemRepo.save(item);
    } else {
      CartItem item = new CartItem();
      item.setCart(cart);
      item.setBook(book);
      item.setQuantity(quantity);
      cart.getItems().add(item);
      cartItemRepo.save(item);
    }

    return cartRepo.save(cart);
  }

  public void removeFromCart(List<Long> bookIds) {
    cartItemRepo.deleteByBookIdIn(bookIds);
  }
}
