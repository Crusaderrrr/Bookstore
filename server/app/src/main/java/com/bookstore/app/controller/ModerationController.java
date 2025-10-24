package com.bookstore.app.controller;

import com.bookstore.app.dto.ModerationActionRequest;
import com.bookstore.app.dto.ModerationRequestDTO;
import com.bookstore.app.model.CloudinaryUploadResponse;
import com.bookstore.app.model.Genre;
import com.bookstore.app.model.ModerationRequest;
import com.bookstore.app.service.AuthorService;
import com.bookstore.app.service.CloudinaryService;
import com.bookstore.app.service.ModerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/moderation")
public class ModerationController {
    private final ModerationService moderationService;
    private final CloudinaryService cloudinaryService;
    private final AuthorService authorService;

    public ModerationController(ModerationService moderationService,
                                CloudinaryService cloudinaryService, AuthorService authorService) {
        this.moderationService = moderationService;
        this.cloudinaryService = cloudinaryService;
        this.authorService = authorService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ModerationRequestDTO>> getAllModerationRequests() {
        List<ModerationRequestDTO> requests = moderationService.findAll().stream()
                .map(ModerationRequest::toDTO).collect(Collectors.toList());

        return ResponseEntity.ok(requests);
    }

    @GetMapping
    public ResponseEntity<List<ModerationRequestDTO>> getMyModerationRequests(Principal principal) {
        List<ModerationRequestDTO> requests =
                moderationService.finAllRequestsByUsername(principal.getName());
        return ResponseEntity.ok(requests);
    }

    @PostMapping
    @PreAuthorize("hasRole('AUTHOR')")
    public ResponseEntity<String> createModerationRequest(@RequestParam MultipartFile image,
                                                          @RequestParam String title, @RequestParam String description,
                                                          @RequestParam double price, @RequestParam String genre, Principal principal)
            throws IOException {
        CloudinaryUploadResponse imageData = cloudinaryService.uploadFile(image);
        ModerationRequest request = new ModerationRequest();
        request.setTitle(title);
        request.setDescription(description);
        request.setPrice(price);
        request.setGenre(Genre.valueOf(genre));
        request.setAuthor(authorService.getAuthorByUsername(principal.getName()));
        request.setImagePublicId(imageData.getPublicId());
        request.setImageUrl(imageData.getSecureUrl());
        moderationService.saveModerationRequest(request);

        return ResponseEntity.ok("Moderation request created");
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> moderationRequestAction(@PathVariable Long id,
                                                     @RequestBody ModerationActionRequest request) {
        if ("approve".equals(request.getAction())) {
            moderationService.approveModerationRequest(id);
            return ResponseEntity.ok("Moderation request approved");
        } else if ("reject".equals(request.getAction())) {
            if (request.getReason() == null || request.getReason().isBlank()) {
                return ResponseEntity.badRequest().body("Reason is required");
            } else {
                moderationService.rejectModerationRequest(id, request.getReason());
                return ResponseEntity.ok("Moderation request rejected");
            }
        }
        return ResponseEntity.badRequest().body("Invalid action: must be 'approve' or 'reject'");
    }

}
