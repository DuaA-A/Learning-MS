package com.learningsystem.entity;

import java.util.List;

public class QuizSubmission {

    private Long id;
    private Quiz quiz;
    private User student;
    private List<String> submittedAnswers; // Assuming answers are stored as a list of strings.
    private Boolean isGraded; // Field to represent whether the submission has been graded.
    private Integer score;

    // Constructor
    public QuizSubmission( Quiz quiz, User student, List<String> submittedAnswers) {
        //this.id = id;
        this.quiz = quiz;
        this.student = student;
        this.submittedAnswers = submittedAnswers;
        this.isGraded = true;
        this.score = null;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public List<String> getSubmittedAnswers() {
        return submittedAnswers;
    }

    public void setSubmittedAnswers(List<String> submittedAnswers) {
        this.submittedAnswers = submittedAnswers;
    }

    public Boolean getIsGraded() {
        return isGraded;
    }

    public void setIsGraded(Boolean isGraded) {
        this.isGraded = isGraded;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Method to grade the submission (optional)
    public void gradeSubmission(int score) {
        this.isGraded = true;
        this.score = score;
    }

    @Override
    public String toString() {
        return "QuizSubmission{" +
                "id=" + id +
                ", quiz=" + quiz +
                ", student=" + student +
                ", submittedAnswers=" + submittedAnswers +
                ", isGraded=" + isGraded +
                ", score=" + score +
                '}';
    }
}
