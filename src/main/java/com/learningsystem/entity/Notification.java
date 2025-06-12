package com.learningsystem.entity;

import java.time.LocalDateTime;

public class Notification {
    private Long id;
    private String message;
    private String type; // e.g., ASSIGNMENT_GRADED, QUIZ_GRADED, REMINDER
    private boolean isRead;
    private LocalDateTime createdAt;
    private Long userId; // Link to the User entity by ID

    // Default constructor
    public Notification() {
        this.createdAt = LocalDateTime.now(); // Default to current time
    }

    // Parameterized constructor
    public Notification(Long id, String message, String type, boolean isRead, Long userId) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = LocalDateTime.now();
        this.userId = userId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
