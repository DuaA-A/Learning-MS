package com.learningsystem.repository;

import com.learningsystem.entity.Question;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class QuestionRepository {
    private final Map<Long, Question> questionStore = new ConcurrentHashMap<>();
    private Long questionIdCounter = 1L;

    // Save or update a question
    public Question save(Question question) {
        if (question.getId() == null) {
            question.setId(questionIdCounter++);
        }
        questionStore.put(question.getId(), question);
        return question;
    }

    // Find a question by ID
    public Optional<Question> findById(Long id) {
        return Optional.ofNullable(questionStore.get(id));
    }

    // Find all questions by IDs (modified method)
    public List<Question> findAllById(List<Long> ids) {
        List<Question> questions = new ArrayList<>();
        for (Long id : ids) {
            Question question = questionStore.get(id);
            if (question != null) { // Check if the question is not null
                questions.add(question);
            }
        }
        return questions;
    }

    // Find all questions (Question Bank)
    public List<Question> findAll() {
        return new ArrayList<>(questionStore.values());
    }

    // Delete a question by ID
    public void deleteById(Long id) {
        questionStore.remove(id);
    }

    // Find questions by category or other criteria (optional)
    public List<Question> findByCategory(String category) {
        List<Question> result = new ArrayList<>();
        for (Question question : questionStore.values()) {
            if (question.getCategory().equals(category)) {
                result.add(question);
            }
        }
        return result;
    }
}