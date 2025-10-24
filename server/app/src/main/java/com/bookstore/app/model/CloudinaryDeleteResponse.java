package com.bookstore.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CloudinaryDeleteResponse {

    @JsonProperty("result")
    private String result;

}
