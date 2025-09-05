package com.claude.dto;

import com.claude.entity.Todo;
import java.time.LocalDateTime;

public class TodoResponseDto {
    private Long id;
    private String title;
    private String description;
    private Boolean isDone;
    private LocalDateTime createdAt;
    
    // 기본 생성자
    public TodoResponseDto() {}
    
    // Entity로부터 생성하는 생성자
    public TodoResponseDto(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.description = todo.getDescription();
        this.isDone = todo.getIsDone();
        this.createdAt = todo.getCreatedAt();
    }
    
    // 생성자
    public TodoResponseDto(Long id, String title, String description, Boolean isDone, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isDone = isDone;
        this.createdAt = createdAt;
    }
    
    // Getter & Setter
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
