package com.bookstore.app.repo;

import com.bookstore.app.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
  Optional<User> findUserByUsername(String username);

  Optional<User> findUserByEmail(String email);
}
