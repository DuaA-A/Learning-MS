package com.learningsystem.repository;

import com.learningsystem.entity.Quiz;
import com.learningsystem.entity.QuizSubmission;
import com.learningsystem.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class QuizSubmissionRepository {

    private final Map<Long, QuizSubmission> submissionStore = new ConcurrentHashMap<>();
    private Long submissionIdCounter = 1L;

    public QuizSubmission save(QuizSubmission submission) {
        if (submission.getId() == null) {
            submission.setId(submissionIdCounter++);
        }
        submissionStore.put(submission.getId(), submission);
        return submission;
    }


    public Optional<QuizSubmission> findById(Long id) {
        return Optional.ofNullable(submissionStore.get(id));
    }

    public List<QuizSubmission> findByQuiz(Quiz quiz) {
        List<QuizSubmission> submissions = new ArrayList<>();
        for (QuizSubmission submission : submissionStore.values()) {
            if (submission.getQuiz().equals(quiz)) {
                submissions.add(submission);
            }
        }
        return submissions;
    }

////////////////////////////////////////////////////////////////////////////////////
    public List<QuizSubmission> findByStudent(User student) {
        List<QuizSubmission> submissions = new ArrayList<>();
        for (QuizSubmission submission : submissionStore.values()) {
            if (submission.getStudent().equals(student)) {
                submissions.add(submission);
            }
        }
        return submissions;
    }
/////////////////////////////////////////////////////////////////////////////////////////////
    public List<QuizSubmission> findAll() {
        return new ArrayList<>(submissionStore.values());
    }

    public void deleteById(Long id) {
        submissionStore.remove(id);
    }
}
