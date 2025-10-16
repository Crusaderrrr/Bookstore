package com.bookstore.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(exclude = {"image", "cart"})
@ToString(exclude = {"image", "cart"})
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

  @Column(nullable = false)
  private String roles = "ROLE_USER";

  private boolean active;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
  @JsonManagedReference
  private Image image;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
  @JsonManagedReference
  private Cart cart;

  @OneToOne(
      mappedBy = "user",
      cascade = CascadeType.PERSIST,
      fetch = FetchType.LAZY,
      optional = true)
  @JsonIgnore
  private Author author;
}
