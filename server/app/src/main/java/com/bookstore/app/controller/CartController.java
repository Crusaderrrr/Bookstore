package com.bookstore.app.controller;

import com.bookstore.app.model.Cart;
import com.bookstore.app.model.CartItem;
import com.bookstore.app.service.CartService;
import java.security.Principal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {
  private final CartService cartService;

  public CartController(CartService cartService) {
    this.cartService = cartService;
  }

  @PostMapping("/add")
  public ResponseEntity<String> addToCart(
      @RequestParam Long bookId,
      @RequestParam(defaultValue = "1") int quantity,
      Principal principal) {

    String username = principal.getName();
    Cart updatedCart = cartService.addToCart(username, bookId, quantity);

    return ResponseEntity.ok("Book added to cart");
  }

  @GetMapping("/items")
  public ResponseEntity<List<CartItem>> getCartItems(Principal principal) {
    List<CartItem> items = cartService.getCartByUsername(principal.getName()).getItems();

    return ResponseEntity.ok(items);
  }

  @PostMapping("/remove")
  public ResponseEntity<String> removeBooksFromCart(@RequestBody List<Long> bookIds) {
    cartService.removeFromCart(bookIds);

    return ResponseEntity.ok("Books removed from cart");
  }
}
