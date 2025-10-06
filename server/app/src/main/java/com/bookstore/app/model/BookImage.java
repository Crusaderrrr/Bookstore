package com.bookstore.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "book_images")
public class BookImage {
  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String publicId;
  private String url;

  @OneToOne
  @JoinColumn(name = "book_id")
  @JsonBackReference
  private Book book;
}
