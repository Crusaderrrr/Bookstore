package com.bookstore.app.service;

import com.bookstore.app.model.UserVerification;
import com.bookstore.app.repo.UserVerificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationService {
  @Autowired private UserVerificationRepository verificationRepository;

  @Transactional
  public boolean verifyCode(String email, String code) {
    UserVerification userVerification = verificationRepository.findByEmail(email).get();
    if (userVerification != null && userVerification.getVerificationCode().equals(code)) {
      this.deleteVerification(email);
      return true;
    }
    return false;
  }

  public void saveVerification(UserVerification verification) {
    verificationRepository.save(verification);
  }

  public void deleteVerification(String email) {
    verificationRepository.deleteByEmail(email);
  }
}
