package com.learningsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learningsystem.entity.Question;
import com.learningsystem.repository.QuestionRepository;

@Service
public class QuestionBankService {

    @Autowired
    private QuestionRepository questionRepository;

    public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public List<Question> getQuestionsByCategory(String category) {
        return questionRepository.findByCategory(category);
    }

    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public Question updateQuestion(Long id, Question updatedQuestion) {
        return questionRepository.findById(id)
                .map(existingQuestion -> {
                    existingQuestion.setQuestionText(updatedQuestion.getQuestionText());
                    existingQuestion.setCorrectAnswer(updatedQuestion.getCorrectAnswer());
                    existingQuestion.setOptions(updatedQuestion.getOptions());
                    existingQuestion.setCategory(updatedQuestion.getCategory());
                    return questionRepository.save(existingQuestion);
                })
                .orElseThrow(() -> new RuntimeException("Question not found with ID: " + id));
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    public List<Question> getQuestionsByIds(List<Long> ids) {
        return questionRepository.findAllById(ids);
    }
}
