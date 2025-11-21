package com.bookstore.app.controller;

import com.bookstore.app.model.Image;
import com.bookstore.app.service.ImageService;
import com.bookstore.app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminController.class, useDefaultFilters = false)
@Import(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AdminControllerTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    UserService userService;
    @MockitoBean
    ImageService imageService;

    @Test
    public void deleteUserWithImagesShouldReturnOk() throws Exception {
        List<Integer> userIds = Arrays.asList(1, 2, 3);
        String expectedResponse = "Users deleted";

        when(imageService.findImageByUserId(1)).thenReturn(mock(Image.class));
        when(imageService.findImageByUserId(2)).thenReturn(null);
        when(imageService.findImageByUserId(3)).thenReturn(null);

        doNothing().when(imageService).deleteImageByUserId(1);
        doNothing().when(userService).deleteUsersById(userIds);

        mockMvc.perform(delete("/users/delete").contentType(MediaType.APPLICATION_JSON)
                        .content(userIds.toString())).andExpect(status().isOk())
                .andExpect(content().string("User deleted"));

        verify(imageService).deleteImageByUserId(1);
        verify(imageService, never()).deleteImageByUserId(2);
        verify(imageService, never()).deleteImageByUserId(3);
        verify(userService).deleteUsersById(userIds);
    }
}
