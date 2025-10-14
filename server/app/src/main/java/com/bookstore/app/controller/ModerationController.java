package com.bookstore.app.controller;

import com.bookstore.app.dto.ModerationRequestDTO;
import com.bookstore.app.model.Genre;
import com.bookstore.app.model.ModerationRequest;
import com.bookstore.app.service.AuthorService;
import com.bookstore.app.service.CloudinaryService;
import com.bookstore.app.service.ModerationService;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/moderation")
public class ModerationController {
  private final ModerationService moderationService;
  private final CloudinaryService cloudinaryService;
  private final AuthorService authorService;

  public ModerationController(
      ModerationService moderationService,
      CloudinaryService cloudinaryService,
      AuthorService authorService) {
    this.moderationService = moderationService;
    this.cloudinaryService = cloudinaryService;
    this.authorService = authorService;
  }

  @GetMapping("/requests/all")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<ModerationRequestDTO>> getAllModerationRequests() {
    List<ModerationRequestDTO> requests =
        moderationService.findAll().stream()
            .map(ModerationRequest::toDTO)
            .collect(Collectors.toList());

    return ResponseEntity.ok(requests);
  }

  @GetMapping("/requests/my")
  public ResponseEntity<List<ModerationRequestDTO>> getMyModerationRequests(Principal principal) {
    List<ModerationRequestDTO> requests =
        moderationService.finAllRequestsByUsername(principal.getName());
    return ResponseEntity.ok(requests);
  }

  @PostMapping("/requests/new")
  @PreAuthorize("hasRole('AUTHOR')")
  public ResponseEntity<String> createModerationRequest(
      @RequestParam MultipartFile image,
      @RequestParam String title,
      @RequestParam String description,
      @RequestParam double price,
      @RequestParam String genre,
      Principal principal)
      throws IOException {
    Map imageData = cloudinaryService.uploadFile(image);
    ModerationRequest request = new ModerationRequest();
    request.setTitle(title);
    request.setDescription(description);
    request.setPrice(price);
    request.setGenre(Genre.valueOf(genre));
    request.setAuthor(authorService.getAuthorByUsername(principal.getName()));
    request.setImagePublicId((String) imageData.get("public_id"));
    request.setImageUrl((String) imageData.get("secure_url"));
    moderationService.saveModerationRequest(request);

    return ResponseEntity.ok("Moderation request created");
  }

  @PostMapping("/requests/{id}/approve")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> approveModerationRequest(@PathVariable Long id) {
    moderationService.approveModerationRequest(id);
    return ResponseEntity.ok("Moderation request approved");
  }

  @PostMapping("/requests/{id}/reject")
  public ResponseEntity<String> rejectModerationRequest(
      @PathVariable Long id, @RequestBody String reason) {
    moderationService.rejectModerationRequest(id, reason);
    return ResponseEntity.ok("Moderation request rejected");
  }
}
