package com.bookstore.app.controller;

import com.bookstore.app.service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
  private final UserService userService;

  public AdminController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/delete")
  public ResponseEntity<String> deleteUser(@RequestBody List<Integer> userIds) {
    userService.deleteUsersById(userIds);
    return ResponseEntity.ok("User deleted");
  }
}
