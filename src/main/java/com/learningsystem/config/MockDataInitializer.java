package com.learningsystem.config;

import com.learningsystem.entity.*;
import com.learningsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@Configuration
public class MockDataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssignmentSubmissionRepository submissionRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizSubmissionRepository quizSubmissionRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @PostConstruct
    public void initializeMockData() {
        long currentId = 4;
        // 1. Add Users
        User instructor = new User(currentId++, "instructor1", "instructor@example.com", "password", Role.INSTRUCTOR);
        User student1 = new User(currentId++, "student1", "raghadabsi202@gmail.com", "password", Role.STUDENT);
        User student2 = new User(currentId++, "student2", "raghadabsi202@gmail.com", "password", Role.STUDENT);
        userRepository.save(instructor);
        userRepository.save(student1);
        userRepository.save(student2);

        // 2. Add Courses
        Course course1 = new Course();
        course1.setTitle("Java Programming");
        course1.setDescription("Learn Java from basics to advanced.");
        course1.setInstructor(instructor);
        course1.setStartDate(LocalDate.now());
        course1.setEndDate(LocalDate.now().plusMonths(1));
        course1.setDuration(Duration.ofDays(30));
        course1.setStudents(new HashSet<>(List.of(student1, student2)));
        courseRepository.save(course1);

        Course course2 = new Course();
        course2.setTitle("Data Structures");
        course2.setDescription("Comprehensive guide to data structures.");
        course2.setInstructor(instructor);
        course2.setStartDate(LocalDate.now());
        course2.setEndDate(LocalDate.now().plusMonths(1));
        course2.setDuration(Duration.ofDays(30));
        course2.setStudents(new HashSet<>(List.of(student1)));
        courseRepository.save(course2);

        // 3. Add Lessons
        Lesson lesson1 = new Lesson();
        lesson1.setTitle("Introduction to Java");
        lesson1.setContent("Basics of Java programming.");
        lesson1.setCourse(course1);
        lessonRepository.save(lesson1);

        Lesson lesson2 = new Lesson();
        lesson2.setTitle("Stacks and Queues");
        lesson2.setContent("Understanding stacks and queues.");
        lesson2.setCourse(course2);
        lessonRepository.save(lesson2);

        // 4. Add Assignments
        Assignment assignment1 = new Assignment();
        assignment1.setTitle("Java Basics Assignment");
        assignment1.setDescription("Solve basic Java problems.");
        assignment1.setDueDate(LocalDate.now().plusDays(7));
        assignment1.setCourse(course1);
        assignmentRepository.save(assignment1);

        Assignment assignment2 = new Assignment();
        assignment2.setTitle("Stacks Assignment");
        assignment2.setDescription("Solve stack-related problems.");
        assignment2.setDueDate(LocalDate.now().plusDays(7));
        assignment2.setCourse(course2);
        assignmentRepository.save(assignment2);

        // 5. Add Assignment Submissions
        AssignmentSubmission submission1 = new AssignmentSubmission();
        submission1.setStudent(student1);
        submission1.setAssignment(assignment1);
        submission1.setContent("Completed all questions.");
        submission1.setStatus("Submitted");
        submission1.setGrade(85);
        submission1.setFeedback("Good job.");
        submissionRepository.save(submission1);

        AssignmentSubmission submission2 = new AssignmentSubmission();
        submission2.setStudent(student2);
        submission2.setAssignment(assignment1);
        submission2.setContent("Completed with minor issues.");
        submission2.setStatus("Submitted");
        submission2.setGrade(78);
        submission2.setFeedback("Need improvement.");
        submissionRepository.save(submission2);

        // 6. Add Quizzes
        Quiz quiz1 = new Quiz();
        quiz1.setTitle("Java Basics Quiz");
        quiz1.setDescription("Test your Java basics.");
        quiz1.setCourse(course1);
        quiz1.setQuestions(List.of("What is Java?", "Explain OOP concepts."));
        quizRepository.save(quiz1);

        Quiz quiz2 = new Quiz();
        quiz2.setTitle("Stacks Quiz");
        quiz2.setDescription("Test your knowledge on stacks.");
        quiz2.setCourse(course2);
        quiz2.setQuestions(List.of("What is a stack?", "Explain stack operations."));
        quizRepository.save(quiz2);

        // 7. Add Quiz Submissions
        QuizSubmission quizSubmission1 = new QuizSubmission(quiz1, student1, List.of("Java is a language", "OOP includes inheritance"));
        quizSubmission1.setScore(90);
        quizSubmissionRepository.save(quizSubmission1);

        QuizSubmission quizSubmission2 = new QuizSubmission(quiz1, student2, List.of("Programming language", "Encapsulation"));
        quizSubmission2.setScore(75);
        quizSubmissionRepository.save(quizSubmission2);

        // 8. Add Grades
        Grade grade1 = new Grade(student1, course1, "ASSIGNMENT", 85.0, "Good performance.");
        Grade grade2 = new Grade(student2, course1, "ASSIGNMENT", 78.0, "Needs improvement.");
        Grade grade3 = new Grade(student1, course1, "QUIZ", 90.0, "Excellent work.");
        Grade grade4 = new Grade(student2, course1, "QUIZ", 75.0, "Good attempt.");
        gradeRepository.save(grade1);
        gradeRepository.save(grade2);
        gradeRepository.save(grade3);
        gradeRepository.save(grade4);

        // 9. Add Attendance
        Attendance attendance1 = new Attendance();
        attendance1.setLesson(lesson1);
        attendance1.setStudent(student1);
        attendance1.setAttendedAt(LocalDate.now().atStartOfDay());
        attendanceRepository.save(attendance1);

        Attendance attendance2 = new Attendance();
        attendance2.setLesson(lesson1);
        attendance2.setStudent(student2);
        attendance2.setAttendedAt(LocalDate.now().atStartOfDay());
        attendanceRepository.save(attendance2);

        System.out.println("Mock data initialized successfully!");
    }
}
