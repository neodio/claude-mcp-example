package com.claude.dto;

import jakarta.validation.constraints.NotBlank;

public class TodoUpdateRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    private Boolean isDone;
    
    // 기본 생성자
    public TodoUpdateRequest() {}
    
    // 생성자
    public TodoUpdateRequest(String title, String description, Boolean isDone) {
        this.title = title;
        this.description = description;
        this.isDone = isDone;
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
    
    public Boolean getIsDone() {
        return isDone;
    }
    
    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }
}
