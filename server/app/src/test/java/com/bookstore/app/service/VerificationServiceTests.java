package com.bookstore.app.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bookstore.app.model.UserVerification;
import com.bookstore.app.repo.UserVerificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VerificationServiceTests {
  @Mock private UserVerificationRepository verificationRepository;

  @Spy @InjectMocks private VerificationService verificationService;

  @Test
  public void testVerifyCode() {
    UserVerification verification = new UserVerification();
    verification.setEmail("test@test.com");
    verification.setVerificationCode("123456");

    when(verificationRepository.findByEmail("test@test.com")).thenReturn(verification);

    assertTrue(verificationService.verifyCode("test@test.com", "123456"));

    verify(verificationService).deleteVerification("test@test.com");
    verify(verificationService).verifyCode("test@test.com", "123456");
  }
}
