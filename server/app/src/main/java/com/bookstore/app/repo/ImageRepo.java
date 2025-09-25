package com.bookstore.app.repo;

import com.bookstore.app.model.Image;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepo extends JpaRepository<Image, Integer> {
  Optional<Image> findImageByUserId(int userId);
}
