package com.claude.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
public class Todo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private Boolean isDone = false;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    // 기본 생성자
    public Todo() {
        this.createdAt = LocalDateTime.now();
    }
    
    // 생성자
    public Todo(String title, String description) {
        this.title = title;
        this.description = description;
        this.isDone = false;
        this.createdAt = LocalDateTime.now();
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
    
    // JPA 생명주기 콜백
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
