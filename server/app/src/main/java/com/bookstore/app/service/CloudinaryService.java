package com.bookstore.app.service;

import com.bookstore.app.model.CloudinaryDeleteResponse;
import com.bookstore.app.model.CloudinaryUploadResponse;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ObjectMapper objectMapper;

    public CloudinaryUploadResponse uploadFile(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return objectMapper.convertValue(uploadResult, CloudinaryUploadResponse.class);
    }

    public CloudinaryDeleteResponse deleteFile(String publicId) throws IOException {
        Map deleteResult = ObjectUtils.asMap("invalidate", true);
        return objectMapper.convertValue(deleteResult, CloudinaryDeleteResponse.class);
    }
}