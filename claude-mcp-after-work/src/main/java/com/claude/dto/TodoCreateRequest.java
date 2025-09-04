package com.claude.dto;

import jakarta.validation.constraints.NotBlank;

public class TodoCreateRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    // 기본 생성자
    public TodoCreateRequest() {}
    
    // 생성자
    public TodoCreateRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
