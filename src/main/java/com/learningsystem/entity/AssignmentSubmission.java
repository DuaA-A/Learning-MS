package com.learningsystem.entity;

public class AssignmentSubmission {

    private Long id;
    private User student;
    private Assignment assignment; 
    private String content; 
    private String status; // "Submitted", "Pending Review"
    private int grade; 
    private String feedback; 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getGrade() { 
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getFeedback() { 
        return feedback;
    }

    public void setFeedback(String feedback) { 
        this.feedback = feedback;
    }
    public String getAssignmentTitle() {
        return this.assignment != null ? this.assignment.getTitle() : null;
    }
    
}