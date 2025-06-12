package com.learningsystem.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learningsystem.entity.Course;
import com.learningsystem.entity.Grade;
import com.learningsystem.entity.Question;
import com.learningsystem.entity.Quiz;
import com.learningsystem.entity.QuizSubmission;
import com.learningsystem.entity.User;
import com.learningsystem.repository.CourseRepository;
import com.learningsystem.repository.QuestionRepository;
import com.learningsystem.repository.QuizRepository;
import com.learningsystem.repository.GradeRepository;
import com.learningsystem.repository.QuizSubmissionRepository;
import com.learningsystem.repository.UserRepository;
import com.learningsystem.service.InAppNotificationService;
@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private QuizSubmissionRepository quizSubmissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private InAppNotificationService AppNotificationService;

   
    public Quiz createQuiz(Long courseId, String title, String description, List<Long> questionIds) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        List<Question> selectedQuestions = questionRepository.findAllById(questionIds);

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setDescription(description);
        quiz.setCourse(course);

        Map<Integer, String> correctAnswers = new HashMap<>();
        List<String> questionTexts = new ArrayList<>();

        for (int i = 0; i < selectedQuestions.size(); i++) {
            Question question = selectedQuestions.get(i);
            questionTexts.add(question.getQuestionText());
            correctAnswers.put(i, question.getCorrectAnswer());
        }

        quiz.setQuestions(questionTexts);
        quiz.setCorrectAnswers(correctAnswers);

        return quizRepository.save(quiz);
    }

   
    public List<String> getQuizQuestions(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        return quiz.getQuestions();
    }

  
    public Double calculateScore(Quiz quiz, List<String> studentAnswers) {
        Map<Integer, String> correctAnswers = quiz.getCorrectAnswers();
        int totalQuestions = correctAnswers.size();
        int correctCount = 0;

        for (int i = 0; i < totalQuestions; i++) {
            if (studentAnswers.get(i).equalsIgnoreCase(correctAnswers.get(i))) {
                correctCount++;
            }
        }

        return (double) correctCount / totalQuestions * 100;
    }

    public QuizSubmission submitQuiz(Long quizId, Long studentId, List<String> studentAnswers) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        double score = calculateScore(quiz, studentAnswers);

        QuizSubmission submission = new QuizSubmission(quiz,student,studentAnswers);
        submission.setQuiz(quiz);
        submission.setStudent(student);
        submission.setScore( (int) score);
        submission.setSubmittedAnswers(studentAnswers);

        Grade grade = new Grade(student, quiz.getCourse(), "QUIZ", score, "Auto-graded");

        gradeRepository.save(grade);

        // Send email notification
        String emailSubject = "Quiz Submission Confirmation";
        String emailBody = "Dear " + student.getUsername() + ",\n\nYou have successfully submitted the quiz: " +
                quiz.getTitle() + ". Your score is: " + score + "%.\n\nBest regards,\nLearning System";
        emailService.sendEmail(student.getEmail(), emailSubject, emailBody);


            // Create in-app notification
            String inAppMessage = String.format("Your quiz has been graded. Score: %.2f", score);
        AppNotificationService.createNotification(studentId, inAppMessage, "QUIZ_GRADED");


        return quizSubmissionRepository.save(submission);
    }

 
    public List<QuizSubmission> getSubmissionsForQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        return quizSubmissionRepository.findByQuiz(quiz);
    }


    public Double getAverageScoreForQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        List<QuizSubmission> submissions = quizSubmissionRepository.findByQuiz(quiz);
        return submissions.stream()
                .mapToDouble(QuizSubmission::getScore)
                .average()
                .orElse(0.0);
    }
    

}
