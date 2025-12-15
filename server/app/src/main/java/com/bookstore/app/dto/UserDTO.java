package com.bookstore.app.dto;

import com.bookstore.app.validator.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDTO {

  @NotEmpty(message = "Username is required")
  private String username;

  @Email(message = "Please provide a valid email")
  private String email;

  @NotBlank(message = "Password is required")
  @ValidPassword
  private String password;

  private String roles;

  private boolean active;

  private String image;
}
