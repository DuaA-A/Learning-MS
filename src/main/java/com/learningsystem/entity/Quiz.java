package com.learningsystem.entity;

import java.util.List;
import java.util.Map;

public class Quiz {

    private Long id; 
    private String title; 
    private String description; 
    private Course course;
    private List<String> questions; 
    private Map<Integer, String> correctAnswers; 
    private List<QuizSubmission> submissions; 

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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public Map<Integer, String> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Map<Integer, String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public List<QuizSubmission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<QuizSubmission> submissions) {
        this.submissions = submissions;
    }
}
