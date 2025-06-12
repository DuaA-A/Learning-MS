package com.learningsystem.controller;

import com.learningsystem.entity.AssignmentSubmission;
import com.learningsystem.entity.Attendance;
import com.learningsystem.entity.Course;
import com.learningsystem.entity.CourseContent;
import com.learningsystem.entity.Grade;
import com.learningsystem.entity.QuizSubmission;
import com.learningsystem.entity.User;
import com.learningsystem.repository.UserRepository;
import com.learningsystem.repository.CourseRepository;
import com.learningsystem.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private GradeService gradeService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AssignmentSubmissionService assignmentSubmissionService;
    
    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private EmailService emailService;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/courses/{courseId}/enroll")
    public ResponseEntity<Void> enrollInCourse(@PathVariable Long courseId, Principal principal) {
        String studentName = principal.getName();
        courseService.enrollStudent(courseId, studentName);

        // Send email notification
        User student = userRepository.findByUsername(studentName)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        String emailSubject = "Course Enrollment Confirmation";
        String emailBody = "Dear " + student.getUsername() + ",\n\nYou have successfully enrolled in the course: " +
                courseService.getCourseById(courseId).getTitle() + ".\n\nBest regards,\nLearning System";
        emailService.sendEmail(student.getEmail(), emailSubject, emailBody);

        return ResponseEntity.ok().build();


    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getEnrolledCourses(Principal principal) {
        String studentName = principal.getName();
        List<Course> courses = courseService.getCoursesByStudent(studentName);
        return ResponseEntity.ok(courses);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/courses/{courseId}/content")
    public ResponseEntity<List<CourseContent>> getCourseContent(@PathVariable Long courseId) {
        List<CourseContent> content = courseService.getCourseContent(courseId);
        return ResponseEntity.ok(content);
    }

 @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/grades")
    public ResponseEntity<List<Map<String, Object>>> getStudentGrades(Principal principal) {
        String studentEmail = principal.getName();
        User student = userRepository.findByUsername(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        List<Grade> grades = gradeService.getGradesByStudent(student.getId());

        List<Map<String, Object>> progressReport = grades.stream().map(grade -> {
            Map<String, Object> progress = new HashMap<>();
            progress.put("type", grade.getType());
            progress.put("score", grade.getScore());

//            if (grade.getType().equals("ASSIGNMENT")) {
//                Assignment assignment = grade.getAssignment(); // Assuming getAssignment() is implemented in Grade
//                progress.put("name", assignment.getTitle());
//            } else if (grade.getType().equals("QUIZ")) {
//                Quiz quiz = grade.getQuiz(); // Assuming getQuiz() is implemented in Grade
//                progress.put("name", quiz.getTitle());
//            }

            progress.put("courseName", grade.getCourse().getTitle());
            progress.put("feedback", grade.getFeedback());

            return progress;
        }).collect(Collectors.toList());

        // Calculate total score
        double totalScore = grades.stream()
                .mapToDouble(Grade::getScore)
                .sum();

        // Add total score to the response
        Map<String, Object> totalScoreMap = new HashMap<>();
        totalScoreMap.put("totalScore", totalScore);
        progressReport.add(totalScoreMap);

        return ResponseEntity.ok(progressReport);
    }
    
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<List<String>> getQuizQuestions(@PathVariable Long quizId) {
        List<String> questions = quizService.getQuizQuestions(quizId);
        return ResponseEntity.ok(questions);
    }
    
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/quizzes/{quizId}/submit")
    public ResponseEntity<QuizSubmission> submitQuizAnswers(
            @PathVariable Long quizId,
            @RequestBody List<String> answers,
            Principal principal
    ) {
        String studentEmail = principal.getName();
        User student = userRepository.findByUsername(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        QuizSubmission submission = quizService.submitQuiz(quizId, student.getId(), answers);
        return ResponseEntity.ok(submission);
    }
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/assignments/{assignmentId}/submit")
    public ResponseEntity<AssignmentSubmission> submitAssignment(
            @PathVariable Long assignmentId,
            @RequestBody String content,
            Principal principal
    ) {
        String studentEmail = principal.getName();
        User student = userRepository.findByUsername(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        AssignmentSubmission submission = assignmentSubmissionService.submitAssignment(
                student.getId(), assignmentId, content
        );
        return ResponseEntity.ok(submission);
    }
    
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/assignments/submissions")
    public ResponseEntity<List<AssignmentSubmission>> getStudentSubmissions(Principal principal) {
        String studentEmail = principal.getName();
        User student = userRepository.findByUsername(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        List<AssignmentSubmission> submissions = assignmentSubmissionService.getSubmissionsByStudent(student.getId());
        return ResponseEntity.ok(submissions);
    }
    
    @PreAuthorize("hasRole('STUDENT')")
@GetMapping("/courses/available")
public ResponseEntity<List<Course>> getAvailableCourses(Principal principal) {
    User userDetails = (User) principal;
    long studentId = userDetails.getId();
    List<Course> courses = courseService.getAvailableCoursesByStudentId(studentId);
    return ResponseEntity.ok(courses);
}

    
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/lessons/{lessonId}/mark-attendance")
    public ResponseEntity<String> markAttendance(
            @PathVariable Long lessonId,
            @RequestParam String otp,
            Principal principal
    ){
        String studentName = principal.getName();
        Attendance attendance = attendanceService.markAttendance(lessonId, studentName, otp);
        return ResponseEntity.ok("Attendance marked at: " + attendance.getAttendedAt());
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/students/{studentId}/assignments/grades")
    public ResponseEntity<List<Grade>> getStudentAssignmentGrades(@PathVariable Long studentId) {
        List<Grade> grades = gradeService.getGradesForStudentAssignments(studentId);
        return ResponseEntity.ok(grades);
    }
    
    // @DeleteMapping("/unenroll")
    // public ResponseEntity<?> deleteEnrolledCourse(Long courseId,Long userId ) {
    //     Course course = CourseRepository.findById(courseId);
    //     List<Long> enrolledUsers = course.getEnrolledUsers();
    //     if (enrolledUsers.contains(userId)) {
    //         enrolledUsers.remove(userId);
    //         return ResponseEntity.ok("User unenrolled successfully.");
    //     } else {
    //         return ResponseEntity.ok("User is not enrolled in this course.");
    //     }
    // }

}
