package com.bookstore.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@Table(name = "books")
public class Book {
  @Id @GeneratedValue private Long id;

  @NotBlank(message = "Title is required")
  private String title;

  @NotBlank(message = "Description is required")
  private String description;

  @DecimalMin(value = "0.99", message = "Price must be greater than or equal to 0.99")
  private double price;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate datePosted;

  @ManyToOne private User author;
}
