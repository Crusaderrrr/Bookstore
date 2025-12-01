package com.bookstore.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CloudinaryUploadResponse {

    private String publicId;

    private String secureUrl;
}
