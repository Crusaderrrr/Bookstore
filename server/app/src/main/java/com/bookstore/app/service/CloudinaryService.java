package com.bookstore.app.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudinaryService {

  @Autowired private Cloudinary cloudinary;

  public Map uploadFile(MultipartFile file) throws IOException {
    byte[] fileBytes = file.getBytes();
    Map uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.emptyMap());
    return uploadResult;
  }

  public Map deleteFile(String publicId) throws IOException {
    Map options = ObjectUtils.asMap("invalidate", true);
    return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
  }
}
