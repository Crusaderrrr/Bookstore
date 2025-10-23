package com.bookstore.app.controller;

import com.bookstore.app.model.Image;
import com.bookstore.app.service.ImageService;
import com.bookstore.app.service.UserService;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserService userService;
    private final ImageService imageService;

    public AdminController(UserService userService, ImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUsers(@RequestBody List<Integer> userIds)
            throws IOException {
        for (Integer userId : userIds) {
            System.out.println(userId);
            Image image = imageService.findImageByUserId(userId);
            if (image != null) {
                imageService.deleteImageByUserId(userId);
            }
        }
        userService.deleteUsersById(userIds);
        return ResponseEntity.ok("User deleted");
    }
    @PatchMapping("/make_admin")
    public ResponseEntity<String> makeAdmin(@RequestBody List<Integer> userIds) {
        userService.makeAdmin(userIds);
        return ResponseEntity.ok("User made admin");
    }

    @PatchMapping("/remove_admin")
    public ResponseEntity<String> removeAdmin(@RequestBody List<Integer> userIds) {
        userService.removeAdmin(userIds);
        return ResponseEntity.ok("Admin rights removed");
    }

    @PatchMapping("/block")
    public ResponseEntity<String> blockUsers(@RequestBody List<Integer> userIds) {
        userService.blockUsers(userIds);
        return ResponseEntity.ok("User blocked");
    }

    @PatchMapping("/unblock")
    public ResponseEntity<String> unblockUsers(@RequestBody List<Integer> userIds) {
        userService.unblockUsers(userIds);
        return ResponseEntity.ok("User unblocked");
    }
}
