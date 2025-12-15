package com.bookstore.app.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookstore.app.model.RefreshToken;
import com.bookstore.app.model.User;
import com.bookstore.app.service.JWTService;
import com.bookstore.app.service.RefreshService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

@WebMvcTest(controllers = RefreshController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class RefreshControllerTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    RefreshService refreshService;
    @MockitoBean
    JWTService jwtService;

    @Test
    public void refreshTokenReturnsNewAccessTokenAndRefreshToken() throws Exception {
        String refreshToken = "validRefreshToken";
        String newAccessToken = "newAccessToken";

        RefreshToken token = new RefreshToken();
        token.setToken(refreshToken);

        User user = new User();
        user.setUsername("testuser");

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);

        when(refreshService.getRefreshToken(refreshToken)).thenReturn(token);
        when(refreshService.verifyExpiration(token)).thenReturn(token);
        when(jwtService.generateToken(user)).thenReturn(newAccessToken);
        when(refreshService.getUserFromRefreshToken(refreshToken)).thenReturn(user);

        mockMvc.perform(post("/refresh_token").cookie(refreshCookie)).andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(newAccessToken))
                .andExpect(jsonPath("$.refreshToken").value(refreshToken));
    }

    @Test
    public void refreshTokenExpiredTokenThrowsException() throws Exception {
        String refreshToken = "validRefreshToken";

        RefreshToken token = new RefreshToken();
        token.setToken(refreshToken);

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);

        when(refreshService.getRefreshToken(refreshToken)).thenReturn(token);
        when(refreshService.verifyExpiration(token)).thenThrow(ResponseStatusException.class);

        mockMvc.perform(post("/refresh_token").cookie(refreshCookie))
                .andExpect(status().is4xxClientError());
    }
}
