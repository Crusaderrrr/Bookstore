package com.bookstore.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorDTO {
  private Long id;
  private String name;
  private String surname;
}
