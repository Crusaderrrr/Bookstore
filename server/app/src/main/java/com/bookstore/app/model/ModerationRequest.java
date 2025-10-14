package com.bookstore.app.model;

import com.bookstore.app.dto.AuthorDTO;
import com.bookstore.app.dto.ModerationRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@Table(name = "moderation_requests")
public class ModerationRequest {
  @Id @GeneratedValue private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private Author author;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private double price;

  @Column(name = "image_public_id")
  private String imagePublicId;

  @Column(name = "image_url")
  private String imageUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ModerationStatus status = ModerationStatus.PENDING;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Genre genre;

  @CreationTimestamp private LocalDateTime createdAt;

  @Column(nullable = true)
  private String reason;

  public static ModerationRequestDTO toDTO(ModerationRequest request) {
    if (request == null) {
      return null;
    }

    Author author = request.getAuthor();

    AuthorDTO authorDTO =
        author == null
            ? null
            : new AuthorDTO(author.getId(), author.getName(), author.getSurname());

    ModerationRequestDTO dto = new ModerationRequestDTO();
    dto.setId(request.getId());
    dto.setTitle(request.getTitle());
    dto.setDescription(request.getDescription());
    dto.setPrice(request.getPrice());
    dto.setImagePublicId(request.getImagePublicId());
    dto.setImageUrl(request.getImageUrl());
    dto.setStatus(request.getStatus().name());
    dto.setCreatedAt(request.getCreatedAt());
    dto.setReason(request.getReason());
    dto.setGenre(Genre.valueOf(request.getGenre().name()).name());
    dto.setAuthor(authorDTO);

    return dto;
  }
}
