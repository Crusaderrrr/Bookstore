package com.bookstore.app.controller;

import com.bookstore.app.dto.UserDTO;
import com.bookstore.app.model.AuthResponse;
import com.bookstore.app.model.Author;
import com.bookstore.app.model.Book;
import com.bookstore.app.model.Image;
import com.bookstore.app.model.User;
import com.bookstore.app.service.AuthService;
import com.bookstore.app.service.AuthorService;
import com.bookstore.app.service.BookService;
import com.bookstore.app.service.ImageService;
import com.bookstore.app.service.JWTService;
import com.bookstore.app.service.RefreshService;
import com.bookstore.app.service.UserService;
import com.bookstore.app.service.VerificationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;
  private final AuthService authService;
  private final JWTService jwtService;
  private final ImageService imageService;
  private final RefreshService refreshService;
  private final VerificationService verificationService;
  private final AuthorService authorService;
  private final BookService bookService;

  @Value("${app.jwtRefreshExpirationMs}")
  private Long refreshTokenDurationMs;

  public UserController(
      UserService userService,
      AuthService authService,
      JWTService jwtService,
      ImageService imageService,
      RefreshService refreshService,
      VerificationService verificationService,
      AuthorService authorService,
      BookService bookService) {
    this.userService = userService;
    this.authService = authService;
    this.jwtService = jwtService;
    this.imageService = imageService;
    this.refreshService = refreshService;
    this.verificationService = verificationService;
    this.authorService = authorService;
    this.bookService = bookService;
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(
      @RequestBody User user, HttpServletResponse response) {
    AuthResponse authResponse = authService.verify(user);
    Cookie refreshCookie = new Cookie("refreshToken", authResponse.getRefreshToken());
    refreshCookie.setHttpOnly(true);
    refreshCookie.setSecure(true);
    refreshCookie.setPath("/");
    refreshCookie.setMaxAge(refreshTokenDurationMs.intValue() / 1000);
    response.addCookie(refreshCookie);

    Map<String, String> responseBody = new HashMap<>();
    responseBody.put("accessToken", authResponse.getAccessToken());
    responseBody.put("role", userService.findByUsername(user.getUsername()).getRoles());
    return ResponseEntity.ok(responseBody);
  }

  @GetMapping("/all")
  @PreAuthorize("isAuthenticated()")
  public Iterable<User> getAllUsers() {
    return userService.getAllUsers();
  }

  @GetMapping("/self")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Map<String, Object>> getCurrentUser(Principal principal) {
    User user = userService.findByUsername(principal.getName());
    Map<String, Object> response = new HashMap<>();
    response.put("user", user);
    Optional<Author> authorOpt = authorService.getAuthorOptByUsername(principal.getName());
    if (authorOpt.isPresent()) {
      List<Book> books = bookService.getBooksByAuthor(authorOpt.get());
      response.put("books", books);
    } else {
      response.put("books", Collections.emptyList());
    }
    return ResponseEntity.ok(response);
  }

  @PostMapping("/new")
  public ResponseEntity<?> newUser(
      @Valid @RequestBody UserDTO user, BindingResult result, HttpServletResponse response) {
    if (result.hasErrors()) {
      List<String> errors =
          result.getAllErrors().stream()
              .map(ObjectError::getDefaultMessage)
              .collect(Collectors.toList());
      return ResponseEntity.badRequest().body(errors);
    }
    try {
      User savedUser = userService.saveUser(user);
      Cookie refreshCookie =
          new Cookie(
              "refreshToken", refreshService.createRefreshToken(user.getUsername()).getToken());
      refreshCookie.setHttpOnly(true);
      refreshCookie.setSecure(true);
      refreshCookie.setPath("/");
      refreshCookie.setMaxAge(refreshTokenDurationMs.intValue() / 1000);
      response.addCookie(refreshCookie);

      String accessToken = jwtService.generateToken(savedUser);

      Map<String, String> responseBody = Map.of("accessToken", accessToken);
      return ResponseEntity.ok(responseBody);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
  }

  @PostMapping("/image_upload")
  public ResponseEntity<Image> uploadUserImage(
      @RequestParam("file") MultipartFile file, Principal principal) throws IOException {
    Image image = imageService.modifyImage(principal.getName(), file);
    return ResponseEntity.ok(image);
  }

  @PostMapping("/confirm-email")
  public ResponseEntity<String> confirmEmail(@RequestParam String code, Principal principal) {
    String email = userService.findByUsername(principal.getName()).getEmail();
    if (!verificationService.verifyCode(email, code)) {
      return ResponseEntity.badRequest().body("Invalid verification code");
    }
    userService.findByUsername(principal.getName()).setActive(true);
    userService.saveUser(userService.findByUsername(principal.getName()));
    return ResponseEntity.ok("Email confirmed");
  }
}
