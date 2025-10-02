package com.bookstore.app.repo;

import com.bookstore.app.model.UserVerification;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {
   UserVerification findByEmail(String email);
   Optional<UserVerification> deleteByEmail(String email);
}
