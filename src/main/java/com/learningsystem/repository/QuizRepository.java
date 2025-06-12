package com.learningsystem.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.learningsystem.entity.Course;
import com.learningsystem.entity.Quiz;

@Repository
public class QuizRepository {
    private final Map<Long, Quiz> quizStore = new ConcurrentHashMap<>();
    private Long quizIdCounter = 1L;

    public Quiz save(Quiz quiz) {
        if (quiz.getId() == null) {
            quiz.setId(quizIdCounter++);
        }
        quizStore.put(quiz.getId(), quiz);
        return quiz;
    }

    public Optional<Quiz> findById(Long id) {
        return Optional.ofNullable(quizStore.get(id));
    }

        public List<Quiz> findByCourse(Course course) {
        List<Quiz> quizzes = new ArrayList<>();
        for (Quiz quiz : quizStore.values()) {
            if (quiz.getCourse().equals(course)) {
                quizzes.add(quiz);
            }
        }
        return quizzes;
    }

    public Map<Long, Quiz> findAll() {
        return quizStore;
    }

    public void deleteById(Long id) {
        quizStore.remove(id);
    }
}
