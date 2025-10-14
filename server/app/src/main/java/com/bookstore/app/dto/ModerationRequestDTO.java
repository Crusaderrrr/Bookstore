package com.bookstore.app.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ModerationRequestDTO {
  private Long id;
  private String title;
  private String description;
  private double price;
  private String imagePublicId;
  private String imageUrl;
  private String status;
  private LocalDateTime createdAt;
  private String reason;
  private String genre;
  private AuthorDTO author;
}
