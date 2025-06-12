package com.learningsystem.entity;

import java.util.List;

public class QuizRequest {

    private String title;
    private String description;
    private List<Long> selectedQuestionIds; // This will hold the list of question IDs selected by the instructor

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

    public List<Long> getSelectedQuestionIds() {
        return selectedQuestionIds;
    }

    public void setSelectedQuestionIds(List<Long> selectedQuestionIds) {
        this.selectedQuestionIds = selectedQuestionIds;
    }
}