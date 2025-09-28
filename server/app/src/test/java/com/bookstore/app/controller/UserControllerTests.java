package com.bookstore.app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookstore.app.model.AuthResponse;
import com.bookstore.app.model.User;
import com.bookstore.app.service.AuthService;
import com.bookstore.app.service.ImageService;
import com.bookstore.app.service.JWTService;
import com.bookstore.app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTests {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private UserService userService;

  @MockitoBean private AuthService authService;

  @MockitoBean private JWTService jwtService;

  @MockitoBean private ImageService imageService;

  @Test
  public void testLoginSuccess() throws Exception {
    User user = new User();
    user.setUsername("testuser");
    user.setPassword("password");

    AuthResponse authResponse = new AuthResponse("accessToken", "refreshToken");
    User foundUser = new User();
    foundUser.setRoles("ROLE_USER");

    when(authService.verify(any(User.class))).thenReturn(authResponse);
    when(userService.findByUsername(anyString())).thenReturn(foundUser);

    mockMvc
        .perform(
            post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user))
                .with(user("testuser").roles("USER"))
                .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value("accessToken"))
        .andExpect(jsonPath("$.role").value("ROLE_USER"))
        .andExpect(cookie().exists("refreshToken"))
        .andExpect(cookie().httpOnly("refreshToken", true))
        .andExpect(cookie().secure("refreshToken", true));

    verify(authService).verify(any(User.class));
    verify(userService).findByUsername("testuser");
  }

  @Test
  public void testLoginFailure() throws Exception {
    User user = new User();
    user.setUsername("testuser");
    user.setPassword("wrongpassword");

    when(authService.verify(any(User.class)))
        .thenThrow(new BadCredentialsException("Invalid credentials"));

    mockMvc
        .perform(
            post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user))
                .with(user("testuser").roles("USER"))
                .with(csrf()))
        .andExpect(status().isUnauthorized());

    verify(authService).verify(any(User.class));
    verifyNoInteractions(userService);
  }

  @Test
  @WithMockUser
  public void testGetAllUsers() throws Exception {
    User user1 = new User();
    user1.setUsername("user1");
    User user2 = new User();
    user2.setUsername("user2");

    when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

    mockMvc
        .perform(get("/users/all").with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$.[0].username").value("user1"))
        .andExpect(jsonPath("$.[1].username").value("user2"));

    verify(userService).getAllUsers();
  }

  @Test
  @WithMockUser
  public void testGetCurrentUser() throws Exception {
    User user = new User();
    user.setUsername("testuser");

    when(userService.findByUsername("testuser")).thenReturn(user);

    mockMvc
        .perform(get("/users/self").with(user("testuser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("testuser"));

    verify(userService).findByUsername("testuser");
  }
}
