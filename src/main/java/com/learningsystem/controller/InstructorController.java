package com.learningsystem.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.learningsystem.entity.Assignment;
import com.learningsystem.entity.AssignmentSubmission;
import com.learningsystem.entity.Attendance;
import com.learningsystem.entity.Course;
import com.learningsystem.entity.CourseContent;
import com.learningsystem.entity.Grade;
import com.learningsystem.entity.Lesson;
import com.learningsystem.entity.Question;
import com.learningsystem.entity.Quiz;
import com.learningsystem.entity.QuizRequest;
import com.learningsystem.entity.QuizSubmission;
import com.learningsystem.service.AssignmentService;
import com.learningsystem.service.AssignmentSubmissionService;
import com.learningsystem.service.AttendanceService;
import com.learningsystem.service.CourseContentService;
import com.learningsystem.service.CourseService;
import com.learningsystem.service.GradeService;
import com.learningsystem.service.LessonService;
import com.learningsystem.service.MediaStorageService;
import com.learningsystem.service.QuestionBankService;
import com.learningsystem.service.QuizService;

@RestController
@RequestMapping("/api/instructor")
public class InstructorController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseContentService contentService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private MediaStorageService mediaStorageService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private QuestionBankService questionBankService;

    @Autowired
    private CourseContentService courseContentService;
    

    // Course Management

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course courseRequest, Principal principal) {
        String instructorUsername = principal.getName();
        Course course = courseService.createCourse(
                courseRequest.getTitle(),
                courseRequest.getDescription(),
                instructorUsername,
                courseRequest.getStartDate(),
                courseRequest.getEndDate(),
                courseRequest.getDuration()
        );
        return ResponseEntity.ok(course);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }

    //http://localhost:8080/api/instructor/courses
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getCourses(Principal principal) {
        String instructorEmail = principal.getName();
        List<Course> courses = courseService.getCoursesByInstructor(instructorEmail);
        return ResponseEntity.ok(courses);
    }

    //http://localhost:8080/api/instructor/courses/{courseId}/content
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/courses/{courseId}/content")
    public ResponseEntity<?> addContent(@PathVariable Long courseId, @RequestBody CourseContent contentRequest) {
        try {
            System.out.println("Controller: Adding content for courseId: " + courseId);
            CourseContent content = courseContentService.addContent(
                    courseId,
                    contentRequest.getTitle(),
                    contentRequest.getType(),
                    contentRequest.getUrl()
            );
            System.out.println("Controller: Successfully added content.");
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            System.err.println("Controller: Error occurred - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add content: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @DeleteMapping("/courses/{courseId}/content/{contentId}")
    public ResponseEntity<Void> deleteCourseContent(@PathVariable Long courseId, @PathVariable Long contentId) {
        courseContentService.deleteContent(courseId, contentId);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PutMapping("/courses/{courseId}/content/{contentId}")
    public ResponseEntity<CourseContent> updateCourseContent(@PathVariable Long courseId, @PathVariable Long contentId, @RequestBody CourseContent contentRequest) {
        try {
            CourseContent updatedContent = courseContentService.updateContent(courseId, contentId, contentRequest);
            return ResponseEntity.ok(updatedContent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/courses/{courseId}/upload-media")
    public ResponseEntity<String> uploadMedia(
            @PathVariable Long courseId,
            @RequestParam("file") MultipartFile file
    ){
        String fileUrl = mediaStorageService.uploadFile(file);
        return ResponseEntity.ok(fileUrl);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long courseId) {
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(course);
    }

    // Lesson Management

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/courses/{courseId}/lessons")
    public ResponseEntity<Lesson> addLessonToCourse(@PathVariable Long courseId, @RequestBody Lesson lesson) {
        Lesson addedLesson = lessonService.addLessonToCourse(courseId, lesson);
        return ResponseEntity.ok(addedLesson);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/courses/{courseId}/lessons")
    public ResponseEntity<List<Lesson>> getLessonsForCourse(@PathVariable Long courseId) {
        List<Lesson> lessons = lessonService.getLessonsForCourse(courseId);
        return ResponseEntity.ok(lessons);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/lessons/{lessonId}/generate-otp")
    public ResponseEntity<String> generateOtp(@PathVariable Long lessonId) {
        String otp = attendanceService.generateOtpForLesson(lessonId);
        return ResponseEntity.ok("OTP for the lesson: " + otp);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/instructor/{studentId}/attendance")
    public ResponseEntity<List<Attendance>> getStudentAttendanceRecords(@PathVariable Long studentId) {
        List<Attendance> attendanceRecords = attendanceService.getAttendanceRecordsForStudent(studentId);
        return ResponseEntity.ok(attendanceRecords);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/student/{studentId}/progress")
    public ResponseEntity<Map<String, Object>> trackStudentProgress(@PathVariable Long studentId) {
        Map<String, Object> progress = gradeService.trackProgressForStudent(studentId);
        return ResponseEntity.ok(progress);
    }


    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PutMapping("/courses/{courseId}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long courseId, @RequestBody Course courseRequest) {
        Course updatedCourse = courseService.updateCourse(courseId, courseRequest);
        return ResponseEntity.ok(updatedCourse);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/courses/{courseId}/details")
    public ResponseEntity<Course> viewCourseDetails(@PathVariable Long courseId) {
        Course courseDetails = courseService.getCourseDetails(courseId);
        return ResponseEntity.ok(courseDetails);
    }


    //---------assignment Management
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/courses/{courseId}/assignments")
    public ResponseEntity<Assignment> createAssignment(@PathVariable Long courseId, @RequestBody Assignment assignmentRequest) {
        Assignment assignment = assignmentService.createAssignment(
                courseId,
                assignmentRequest.getTitle(),
                assignmentRequest.getDescription(),
                assignmentRequest.getDueDate().toString()
        );
        return ResponseEntity.ok(assignment);
    }
    
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/assignments/{assignmentId}/grade")
    public ResponseEntity<Grade> gradeAssignment(
            @PathVariable Long assignmentId,
            @RequestParam Long studentId,
            @RequestParam Double score,
            @RequestParam String feedback
    ){
        AssignmentSubmission submission = AssignmentSubmissionService.getSubmissionByAssignmentAndStudent(assignmentId, studentId);
        if (submission == null) {
            throw new RuntimeException("The student has not submitted this assignment.");
        }
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        Course course = assignment.getCourse();
        if (course.getStudents().stream().noneMatch(student -> student.getId().equals(studentId))) {
            throw new RuntimeException("The student is not enrolled in this course.");
        }
        Grade grade = gradeService.assignGradeToAssignment(studentId, assignmentId, score, feedback);
        return ResponseEntity.ok(grade);
    }
     @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/students/{studentId}/assignments/grades")
    public ResponseEntity<List<Grade>> getStudentAssignmentGrades(@PathVariable Long studentId) {
        List<Grade> grades = gradeService.getGradesForStudentAssignments(studentId);
        return ResponseEntity.ok(grades);
    }


    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/assignments/{assignmentId}")
    public ResponseEntity<Assignment> getAssignmentDetails(@PathVariable Long assignmentId) {
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        return ResponseEntity.ok(assignment);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/courses/{courseId}/assignments")
    public ResponseEntity<List<Assignment>> getAllAssignmentsForCourse(@PathVariable Long courseId) {
        List<Assignment> assignments = assignmentService.getAssignmentsForCourse(courseId);
        return ResponseEntity.ok(assignments);
    }

    //-----------quiz Management

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/courses/{courseId}/quizzes")
    public ResponseEntity<Quiz> createQuiz(@PathVariable Long courseId, @RequestBody QuizRequest quizRequest) {
        Quiz quiz = quizService.createQuiz(
                courseId,
                quizRequest.getTitle(),
                quizRequest.getDescription(),
                quizRequest.getSelectedQuestionIds()
        );
        return ResponseEntity.ok(quiz);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/quizzes/{quizId}/grade")
    public ResponseEntity<Grade> gradeQuiz(
            @PathVariable Long quizId,
            @RequestParam Long studentId,
            @RequestParam Double score,
            @RequestParam String feedback
    ) {
        Grade grade = gradeService.assignGradeToQuiz(studentId, quizId, score, feedback);
        return ResponseEntity.ok(grade);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/quizzes/{quizId}/submissions")
    public ResponseEntity<List<QuizSubmission>> getSubmissions(@PathVariable Long quizId) {
        List<QuizSubmission> submissions = quizService.getSubmissionsForQuiz(quizId);
        return ResponseEntity.ok(submissions);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/quizzes/{quizId}/average-score")
    public ResponseEntity<Double> getAverageScore(@PathVariable Long quizId) {
        double averageScore = quizService.getAverageScoreForQuiz(quizId);
        return ResponseEntity.ok(averageScore);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/quizzes/{quizId}/analysis")
    public ResponseEntity<Map<String, Object>> getQuizAnalysis(@PathVariable Long quizId) {
        List<QuizSubmission> submissions = quizService.getSubmissionsForQuiz(quizId);
        double averageScore = quizService.getAverageScoreForQuiz(quizId);
        int highestScore = submissions.stream()
                .mapToInt(QuizSubmission::getScore)
                .max()
                .orElse(0);
        int lowestScore = submissions.stream()
                .mapToInt(QuizSubmission::getScore)
                .min()
                .orElse(0);

        Map<String, Object> analysis = new HashMap<>();
        analysis.put("averageScore", averageScore);
        analysis.put("highestScore", highestScore);
        analysis.put("lowestScore", lowestScore);
        analysis.put("totalSubmissions", submissions.size());

        return ResponseEntity.ok(analysis);
    }

    //--------question Bank Management

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/questions")
    public ResponseEntity<Question> addQuestion(@RequestBody Question questionRequest) {
        Question question = questionBankService.addQuestion(questionRequest);
        return ResponseEntity.ok(question);
    }
    
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/questions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = questionBankService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/questions/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        return questionBankService.getQuestionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PreAuthorize("hasRole('INSTRUCTOR')")
    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionBankService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/questions/category/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable String category) {
        List<Question> questions = questionBankService.getQuestionsByCategory(category);
        return ResponseEntity.ok(questions);
    }
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/attendance/{studentId}/course/{courseId}")
    public List<Attendance> getAttendanceForStudentInCourse(@PathVariable Long studentId, 
                                                           @PathVariable Long courseId) {
        // Use the service to get the attendance for the student in the specified course
        return attendanceService.getAttendanceForStudentInCourse(courseId, studentId);
    }
}
