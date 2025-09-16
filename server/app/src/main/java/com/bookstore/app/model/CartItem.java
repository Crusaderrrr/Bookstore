package com.bookstore.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "cart_item")
public class CartItem {
  @Id @GeneratedValue private Long id;

  @ManyToOne private Cart cart;

  @ManyToOne private Book book;

  private int quantity;
}
