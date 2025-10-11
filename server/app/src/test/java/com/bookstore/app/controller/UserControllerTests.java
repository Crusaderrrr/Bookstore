package com.bookstore.app.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookstore.app.dto.UserDTO;
import com.bookstore.app.model.AuthResponse;
import com.bookstore.app.model.RefreshToken;
import com.bookstore.app.model.User;
import com.bookstore.app.service.AuthService;
import com.bookstore.app.service.AuthorService;
import com.bookstore.app.service.BookService;
import com.bookstore.app.service.ImageService;
import com.bookstore.app.service.JWTService;
import com.bookstore.app.service.RefreshService;
import com.bookstore.app.service.UserService;
import com.bookstore.app.service.VerificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.Principal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper objectMapper;

  @MockitoBean UserService userService;
  @MockitoBean AuthService authService;
  @MockitoBean JWTService jwtService;
  @MockitoBean ImageService imageService;
  @MockitoBean RefreshService refreshService;
  @MockitoBean VerificationService verificationService;
  @MockitoBean AuthorService authorService;
  @MockitoBean BookService bookService;

  @Test
  public void testLogin() throws Exception {
    User user = new User();
    user.setUsername("testuser");
    user.setPassword("testpassword");

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonRequest = objectMapper.writeValueAsString(user);

    User savedUser = new User();
    savedUser.setUsername("testuser");
    savedUser.setPassword("testpassword");
    savedUser.setRoles("ROLE_USER");

    when(userService.saveUser(user)).thenReturn(savedUser);

    when(authService.verify(user)).thenReturn(new AuthResponse("accessToken", "refreshToken"));

    when(userService.findByUsername("testuser")).thenReturn(savedUser);

    mockMvc
        .perform(post("/users/login").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value("accessToken"))
        .andExpect(jsonPath("$.role").value("ROLE_USER"))
        .andExpect(cookie().exists("refreshToken"));
  }

  @Test
  public void testNewUser() throws Exception {
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername("testuser");
    userDTO.setPassword("valiD123#");

    String jsonRequest = objectMapper.writeValueAsString(userDTO);

    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setToken("refreshToken");

    User savedUser = new User();
    savedUser.setUsername("testuser");
    savedUser.setPassword("valiD123#");

    when(userService.saveUser(userDTO)).thenReturn(savedUser);

    when(refreshService.createRefreshToken("testuser")).thenReturn(refreshToken);

    when(jwtService.generateToken(savedUser)).thenReturn("accessToken");

    mockMvc
        .perform(post("/users/new").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value("accessToken"))
        .andExpect(cookie().exists("refreshToken"))
        .andExpect(cookie().secure("refreshToken", true));
  }

  @Test
  public void testNewUserWithWrongPasswordThrowsException() throws Exception {
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername("testuser");
    userDTO.setPassword("invalidpsw");

    String jsonRequest = objectMapper.writeValueAsString(userDTO);

    mockMvc
        .perform(post("/users/new").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void testConfirmEmail() throws Exception {
    String code = "123456";
    String username = "testuser";
    String email = "test@test.com";
    User user = new User();
    user.setUsername(username);
    user.setActive(false);
    user.setEmail(email);

    Principal principal = () -> username;

    when(userService.findByUsername(username)).thenReturn(user);

    when(verificationService.verifyCode(email, code)).thenReturn(true);

    mockMvc
        .perform(post("/users/confirm-email").param("code", code).principal(principal))
        .andExpect(status().isOk())
        .andExpect(content().string("Email confirmed"));
  }

    @Test
  public void testConfirmEmailInvalidCodeThrowsException() throws Exception {
    String code = "123456";
    String username = "testuser";
    String email = "test@test.com";
    User user = new User();
    user.setUsername(username);
    user.setActive(false);
    user.setEmail(email);

    Principal principal = () -> username;

    when(userService.findByUsername(username)).thenReturn(user);

    when(verificationService.verifyCode(email, code)).thenReturn(false);

    mockMvc
        .perform(post("/users/confirm-email").param("code", code).principal(principal))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string("Invalid verification code"));
  }
}
