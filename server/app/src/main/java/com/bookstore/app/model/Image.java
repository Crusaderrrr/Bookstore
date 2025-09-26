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
@Table(name = "images")
public class Image {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  private String publicId;
  private String url;

  @OneToOne
  @JoinColumn(name = "user_id")
  @JsonBackReference
  private User user;
}
