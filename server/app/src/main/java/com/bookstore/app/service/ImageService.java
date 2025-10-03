package com.bookstore.app.service;

import com.bookstore.app.model.Image;
import com.bookstore.app.model.User;
import com.bookstore.app.repo.ImageRepo;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
  private final ImageRepo imageRepo;
  private final CloudinaryService cloudinaryService;
  private final UserService userService;

  public ImageService(
      ImageRepo imageRepo, CloudinaryService cloudinaryService, UserService userService) {
    this.imageRepo = imageRepo;
    this.cloudinaryService = cloudinaryService;
    this.userService = userService;
  }

  public void addImage(String username, MultipartFile file) throws IOException {
    User user = userService.findByUsername(username);
    Map imageData = cloudinaryService.uploadFile(file);
    String publicId = imageData.get("public_id").toString();
    String imageUrl = imageData.get("secure_url").toString();
    Image image = new Image();
    image.setPublicId(publicId);
    image.setUrl(imageUrl);
    image.setUser(user);
    imageRepo.save(image);
  }

  public Image findImageByUserId(Integer userId) {
    Optional<Image> imageOpt = imageRepo.findImageByUserId(userId);
    if (imageOpt.isPresent()) {
      return imageOpt.get();
    } else {
      return null;
    }
  }

  @Transactional
  public void modifyImage(String username, MultipartFile file) throws IOException {
    User user = userService.findByUsername(username);
    Optional<Image> existingImageOpt = imageRepo.findImageByUserId(user.getId());
    if (existingImageOpt.isPresent()) {
      Image image = existingImageOpt.get();
      cloudinaryService.deleteFile(image.getPublicId());
      Map imageData = cloudinaryService.uploadFile(file);
      String newPublicId = imageData.get("public_id").toString();
      String newUrl = imageData.get("secure_url").toString();

      image.setPublicId(newPublicId);
      image.setUrl(newUrl);

      imageRepo.save(image);
    } else {
      addImage(username, file);
    }
  }

  @Transactional
  public void deleteImagesByUserIds(List<Integer> userIds) throws IOException {
    userIds.forEach(
        userId -> {
          try {
            deleteImageByUserId(userId);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
  }

  public void deleteImageByUserId(int userId) throws IOException {
    Image image = findImageByUserId(userId);
    cloudinaryService.deleteFile(image.getPublicId());
  }
}
