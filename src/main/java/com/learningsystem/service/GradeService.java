package com.learningsystem.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learningsystem.entity.Assignment;
import com.learningsystem.entity.AssignmentSubmission;
import com.learningsystem.entity.Grade;
import com.learningsystem.entity.Quiz;
import com.learningsystem.entity.User;
import com.learningsystem.repository.AssignmentRepository;
import com.learningsystem.repository.AssignmentSubmissionRepository;
import com.learningsystem.repository.GradeRepository;
import com.learningsystem.repository.QuizRepository;
import com.learningsystem.repository.UserRepository;
import com.learningsystem.service.InAppNotificationService;


@Service
public class GradeService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private QuizRepository quizRepository;

        @Autowired
        private GradeRepository gradeRepository;
        
        @Autowired
        private AssignmentSubmissionRepository assignmentSubmissionRepository;

        @Autowired
        private EmailService emailService;

        @Autowired
        private AssignmentRepository assignmentRepository;
        private final Map<Long, Grade> grades = new HashMap<>(); 
        private final Map<Long, Assignment> assignments;
        private final Map<Long, Quiz> quizzes; 
        private final Map<Long, User> users;
        private final AtomicLong gradeIdGenerator = new AtomicLong(1);
        @Autowired
        private InAppNotificationService AppNotificationService;
        
        public GradeService(Map<Long, Assignment> assignments, Map<Long, Quiz> quizzes, Map<Long, User> users) {
                this.assignments = assignments;
                this.quizzes = quizzes;
                this.users = users;
        }

        public Grade assignGradeToAssignment(Long studentId, Long assignmentId, Double score, String feedback) {
                User student = userRepository.findById(studentId)
                        .orElseThrow(() -> new RuntimeException("Student not found"));
                Assignment assignment = assignmentRepository.findById(assignmentId)
                        .orElseThrow(() -> new RuntimeException("Assignment not found"));
                Grade grade = new Grade();
                grade.setStudent(student);
                grade.setCourse(assignment.getCourse());
                grade.setType("ASSIGNMENT");
                grade.setScore(score);
                grade.setFeedback(feedback);


                // Send email notification
                String subject = "Your assignment has been graded";
                String emailBody = String.format("Your assignment has been graded.\nScore: %.2f\nFeedback: %s", score, feedback);
                emailService.sendEmail(student.getEmail(), subject, emailBody);

                // Create in-app notification
                String inAppMessage = String.format("Your assignment has been graded. Score: %.2f", score);
                AppNotificationService.createNotification(studentId, inAppMessage, "ASSIGNMENT_GRADED");

                return gradeRepository.save(grade);


        }

        public Grade assignGradeToQuiz(Long studentId, Long quizId, Double score, String feedback) {
                User student = userRepository.findById(studentId)
                        .orElseThrow(() -> new RuntimeException("Student not found"));
                Quiz quiz = quizRepository.findById(quizId)
                        .orElseThrow(() -> new RuntimeException("Quiz not found"));
                Grade grade = new Grade();
                grade.setId(gradeIdGenerator.getAndIncrement());
                grade.setStudent(student);
                grade.setType("QUIZ");
                grade.setScore(score);
                grade.setFeedback(feedback);
                grades.put(grade.getId(), grade);
                return grade;
        }

        public List<Grade> getGradesByStudent(Long studentId) {
                User student = userRepository.findById(studentId)
                        .orElseThrow(() -> new RuntimeException("Student not found"));
                return gradeRepository.findAll().values().stream()
                        .filter(grade -> grade.getStudent().equals(student))
                        .toList();  
        }


        public List<Grade> getGradesByAssignment(Long assignmentId) {
                Assignment assignment = assignmentRepository.findById(assignmentId)
                        .orElseThrow(() -> new RuntimeException("Assignment not found"));
                return gradeRepository.findAll().values().stream()
                        .filter(grade -> grade.getType()!= null && grade.getType().equals(assignment))
                        .toList();
        }


        public List<Grade> getGradesByQuiz(Long quizId) {
                Quiz quiz = quizRepository.findById(quizId)
                        .orElseThrow(() -> new RuntimeException("Quiz not found"));
                return gradeRepository.findAll().values().stream()
                        .filter(grade -> grade.getType()!= null && grade.getType().equals(quiz))
                        .toList();
        }


        public List<Grade> getGradesByStudentEmail(String studentEmail) {
                User student = userRepository.findByEmail(studentEmail)
                        .orElseThrow(() -> new RuntimeException("Student not found"));
                return gradeRepository.findAll().values().stream()
                        .filter(grade -> grade.getStudent().equals(student))
                        .toList();
        }

        public Grade assignGradeToSubmission(Long studentId, Long submissionId, Double score, String feedback) {
                User student = userRepository.findById(studentId)
                        .orElseThrow(() -> new RuntimeException("Student not found"));
                AssignmentSubmission submission = assignmentSubmissionRepository.findById(submissionId) // Use the repository instance
                        .orElseThrow(() -> new RuntimeException("Submission not found"));
                Grade grade = new Grade();
                grade.setStudent(student);
                grade.setType("ASSIGNMENT");
                grade.setScore(score);
                grade.setFeedback(feedback);
                return gradeRepository.save(grade);
        }

        public int calculateTotalGradeForStudent(User student) {
                return assignmentSubmissionRepository.findByStudent(student).stream()
                        .mapToInt(AssignmentSubmission::getGrade) 
                        .sum();
        }

        public List<Grade> getGradesForStudent(Long courseId, Long studentId, String type) {
                return gradeRepository.findByCourseIdAndStudentIdAndType(courseId, studentId, type);
        }

        public List<Grade> getGradesForStudentAssignments(Long studentId) {
                User student = userRepository.findById(studentId)
                        .orElseThrow(() -> new RuntimeException("Student not found"));
                return gradeRepository.findAll().values().stream()
                        .filter(grade -> grade.getStudent().equals(student) && "ASSIGNMENT".equals(grade.getType()))
                        .toList();
        }

        public Map<String, Object> trackProgressForStudent(Long studentId) {
                User student = userRepository.findById(studentId)
                        .orElseThrow(() -> new RuntimeException("Student not found"));

                List<Grade> grades = gradeRepository.findAll().values().stream()
                        .filter(grade -> grade.getStudent().equals(student))
                        .toList();

                int totalAssignments = (int) grades.stream()
                        .filter(grade -> "ASSIGNMENT".equals(grade.getType()))
                        .count();

                int totalQuizzes = (int) grades.stream()
                        .filter(grade -> "QUIZ".equals(grade.getType()))
                        .count();

                double averageAssignmentScore = grades.stream()
                        .filter(grade -> "ASSIGNMENT".equals(grade.getType()))
                        .mapToDouble(Grade::getScore)
                        .average()
                        .orElse(0.0);

                double averageQuizScore = grades.stream()
                        .filter(grade -> "QUIZ".equals(grade.getType()))
                        .mapToDouble(Grade::getScore)
                        .average()
                        .orElse(0.0);

                Map<String, Object> progress = new HashMap<>();
                progress.put("totalAssignments", totalAssignments);
                progress.put("totalQuizzes", totalQuizzes);
                progress.put("averageAssignmentScore", averageAssignmentScore);
                progress.put("averageQuizScore", averageQuizScore);

                return progress;
        }
}
