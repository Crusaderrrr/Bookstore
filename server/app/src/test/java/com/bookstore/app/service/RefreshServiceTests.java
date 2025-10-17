package com.bookstore.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.bookstore.app.model.RefreshToken;
import com.bookstore.app.model.User;
import com.bookstore.app.repo.RefreshTokenRepository;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
public class RefreshServiceTests {
    @Mock
    private UserService userService;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @InjectMocks
    private RefreshService refreshService;

    @Test
    public void testCreateRefreshToken() {
        User user = new User();
        user.setUsername("testUser");

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken("testToken");

        when(userService.findByUsername("testUser")).thenReturn(user);
        doNothing().when(refreshTokenRepository).deleteByUser(user);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(token);

        RefreshToken createdToken = refreshService.createRefreshToken("testUser");

        assertNotNull(createdToken);
        assertEquals(user, createdToken.getUser());

        verify(refreshTokenRepository).deleteByUser(user);
        verify(refreshTokenRepository).flush();
        verify(refreshTokenRepository).save(any(RefreshToken.class));
        verify(userService).findByUsername("testUser");
    }

    @Test
    public void testCreateRefreshTokenUserNotFound() {
        when(userService.findByUsername("nonExistentUser")).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            refreshService.createRefreshToken("nonExistentUser");
        });

        verify(userService).findByUsername("nonExistentUser");
        verifyNoInteractions(refreshTokenRepository);
    }

    @Test
    public void testVerifyExpiration() {
        RefreshToken validToken = new RefreshToken();
        validToken.setExpiryDate(Instant.now().plusSeconds(60));

        RefreshToken result = refreshService.verifyExpiration(validToken);

        assertNotNull(result);
        assertEquals(validToken, result);

        verify(refreshTokenRepository, never()).delete(any(RefreshToken.class));
    }

    @Test
    public void testVerifyExpirationExpiredToken() {
        RefreshToken invalidToken = new RefreshToken();
        invalidToken.setExpiryDate(Instant.now().minusSeconds(10));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            refreshService.verifyExpiration(invalidToken);
        });

        assertEquals(400, exception.getStatusCode().value());
        assertEquals("Please make a new signin request", exception.getReason());

        verify(refreshTokenRepository).delete(invalidToken);
    }
}
