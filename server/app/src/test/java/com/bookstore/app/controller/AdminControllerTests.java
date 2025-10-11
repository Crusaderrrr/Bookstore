package com.bookstore.app.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookstore.app.model.Image;
import com.bookstore.app.service.ImageService;
import com.bookstore.app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AdminController.class, useDefaultFilters = false)
@Import(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AdminControllerTests {
  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper objectMapper;

  @MockitoBean UserService userService;
  @MockitoBean ImageService imageService;

  @Test
  public void deleteUserWithImagesShouldReturnOk() throws Exception {
    List<Integer> userIds = Arrays.asList(1, 2, 3);

    when(imageService.findImageByUserId(1)).thenReturn(new Image());
    when(imageService.findImageByUserId(2)).thenReturn(null);
    when(imageService.findImageByUserId(3)).thenReturn(null);

    doNothing().when(imageService).deleteImageByUserId(1);
    doNothing().when(userService).deleteUsersById(userIds);

    mockMvc
        .perform(
            post("/users/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userIds.toString()))
        .andExpect(status().isOk())
        .andExpect(content().string("User deleted"));

    verify(imageService).deleteImageByUserId(1);
    verify(imageService, never()).deleteImageByUserId(2);
    verify(imageService, never()).deleteImageByUserId(3);
    verify(userService).deleteUsersById(userIds);
  }
}
