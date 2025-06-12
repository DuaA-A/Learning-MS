package com.learningsystem.entity;

public class Grade {
    private Long id;
    private User student;  
    private Course course; 
    private String type;   // QUIZ or ASSIGNMENT
    private Double score;
    private String feedback;

    public Grade() {}

    public Grade(User student, Course course, String type, Double score, String feedback) {
        this.student = student;
        this.course = course;
        this.type = type;
        this.score = score;
        this.feedback = feedback;
    }

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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
