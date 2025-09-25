package com.bookstore.app.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @NotEmpty(message = "Username is required")
  @Column(unique = true)
  private String username;

  @Email(message = "Please provide a valid email")
  @Column(unique = true)
  private String email;

  private String password;

  private String roles;
  private boolean active;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private Image image;
}
