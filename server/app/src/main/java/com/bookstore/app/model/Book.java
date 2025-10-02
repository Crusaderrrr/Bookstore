package com.bookstore.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
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

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "author_id")
  @JsonBackReference
  private Author author;

  @JsonProperty("authorInfo")
  public Map<String, String> getAuthorInfo() {
    Map<String, String> info = new HashMap<>();
    info.put("name", author.getName());
    info.put("surname", author.getSurname());
    return info;
  }
}
