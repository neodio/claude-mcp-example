package com.claude.dto;

public class TodoCreateDto {
    private String title;
    private String description;
    
    // 기본 생성자
    public TodoCreateDto() {}
    
    // 생성자
    public TodoCreateDto(String title, String description) {
        this.title = title;
        this.description = description;
    }
    
    // Getter & Setter
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
