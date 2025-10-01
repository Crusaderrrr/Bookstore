package com.bookstore.app.repo;

import com.bookstore.app.model.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {
   UserVerification findByEmail(String email);
}
