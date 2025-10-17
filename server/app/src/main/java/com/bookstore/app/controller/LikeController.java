package com.bookstore.app.controller;

import com.bookstore.app.dto.BookLikeRequest;
import com.bookstore.app.service.LikeService;
import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/likes")
@RequestMapping("/likes")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addLike(@RequestBody BookLikeRequest request,
            Principal principal) {
        likeService.addLikeForBook(request.getBookId(), principal.getName());
        return ResponseEntity.ok("Like added");
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeLike(@RequestBody BookLikeRequest request) {
        likeService.removeLikeForBook(request.getBookId());
        return ResponseEntity.ok("Like removed");
    }
}
