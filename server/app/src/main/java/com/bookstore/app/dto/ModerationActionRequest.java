package com.bookstore.app.dto;

import lombok.Data;

@Data
public class ModerationActionRequest {
    private String action;
    private String reason;
}
