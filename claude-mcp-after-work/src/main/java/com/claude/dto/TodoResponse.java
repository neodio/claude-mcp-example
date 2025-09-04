package com.claude.dto;

import com.claude.entity.Todo;

public class TodoResponse {
    
    private Long id;
    private String title;
    private String description;
    private Boolean isDone;
    
    // 기본 생성자
    public TodoResponse() {}
    
    // 생성자
    public TodoResponse(Long id, String title, String description, Boolean isDone) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isDone = isDone;
    }
    
    // Entity를 받는 생성자
    public TodoResponse(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.description = todo.getDescription();
        this.isDone = todo.getIsDone();
    }
    
    // Getters and Setters
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
}
