package com.learningsystem;

import com.learningsystem.entity.Assignment;
import com.learningsystem.entity.AssignmentSubmission;
import com.learningsystem.entity.User;
import com.learningsystem.repository.AssignmentSubmissionRepository;
import com.learningsystem.repository.CourseRepository;
import com.learningsystem.repository.UserRepository;
import com.learningsystem.service.AssignmentSubmissionService;
import com.learningsystem.service.CustomUserService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class LearningManagementSystemApplicationTests {

    @MockBean
    private CustomUserService customUserDetailsService;

    @MockBean
    private AssignmentSubmissionService assignmentSubmissionService;

    @MockBean
    private AssignmentSubmissionRepository assignmentSubmissionRepository;

    @MockBean
    private UserRepository userRepository;
	
	@MockBean
    private CourseRepository courseRepository;

    @Test
    @WithMockUser(username = "student@example.com", roles = {"STUDENT"})
    void testGetStudentSubmissions() {
        String studentEmail = "student@example.com";
        User student = new User();
        student.setId(1L);
        student.setUsername(studentEmail);
        Assignment assignment = new Assignment();
        assignment.setTitle("Assignment 1");
        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setId(1L);
        submission.setAssignment(assignment);
        submission.setStudent(student);
        when(userRepository.findByUsername(studentEmail)).thenReturn(Optional.of(student));
        when(assignmentSubmissionRepository.findByStudent(student))
                .thenReturn(Collections.singletonList(submission));
        List<AssignmentSubmission> submissions = assignmentSubmissionRepository.findByStudent(student);
        assertEquals(1, submissions.size());
        assertEquals("Assignment 1", submissions.get(0).getAssignment().getTitle());
    }

    @Test
    void testUserNotFound() {
        String studentEmail = "nonexistent@example.com";
        when(userRepository.findByUsername(studentEmail)).thenReturn(Optional.empty());
        try {
            userRepository.findByUsername(studentEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } catch (RuntimeException e) {
            assertEquals("User not found", e.getMessage());
        }
    }

    @Test
    void testFindAssignmentsByStudent() {
        User student = new User();
        student.setId(1L);
        student.setUsername("student@example.com");
        Assignment assignment = new Assignment();
        assignment.setTitle("Assignment1");
        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setId(1L);
        submission.setAssignment(assignment);
        submission.setStudent(student);
        when(assignmentSubmissionRepository.findByStudent(student))
                .thenReturn(Collections.singletonList(submission));
        List<AssignmentSubmission> submissions = assignmentSubmissionRepository.findByStudent(student);
        assertEquals(1, submissions.size());
        assertEquals("Assignment 1", submissions.get(0).getAssignment().getTitle());
        verify(assignmentSubmissionRepository, times(1)).findByStudent(student);
    }

	// @Test
	// void testViewAssignmentsForEnrolledUser() {
	// 	// Create course
	// 	Course course = new Course();
	// 	course.setTitle("Math 101");
	// 	course = courseRepository.save(course);
	// 	// Create user with STUDENT role
	// 	User user = new User();
	// 	user.setUsername("JohnDoe");
	// 	user.setEmail("johndoe@example.com");
	// 	user.setPassword("password123");
	// 	user.setRole(Role.STUDENT);
	// 	user = userRepository.save(user);
	// 	// Enroll user in course
	// 	course.addStudent(user); // Assuming Course has an addStudent method
	// 	courseRepository.save(course);
	// 	// Create assignment
	// 	Assignment assignment = new Assignment();
	// 	assignment.setTitle("Homework 1");
	// 	assignment.setCourse(course); // Assuming Assignment is linked to a Course
	// 	assignment = assignmentRepository.save(assignment);
	// 	// Create and link assignment submission
	// 	AssignmentSubmission submission = new AssignmentSubmission();
	// 	submission.setAssignment(assignment);
	// 	submission.setStudent(user);
	// 	assignment.addSubmission(submission); // Assuming Assignment has an addSubmission method
	// 	assignmentRepository.save(assignment); // Save updated assignment with submission
	// 	// Verify user can see assignment submissions
	// 	List<AssignmentSubmission> submissions = assignment.getSubmissions(); // Retrieve submissions from assignment
	// 	assertTrue(submissions.contains(submission));
	// }


	// @Test
	// void testStudentEnrollmentInCourse() {
	// 	Course course = new Course();
	// 	course.setId(1L);
	// 	course.setTitle("Math 101");
	// 	User student = new User();
	// 	student.setId(1L);
	// 	student.setUsername("student@example.com");
	// 	when(UserRepository.findById(student.getId())).thenReturn(Optional.of(student));
	// 	when(AssignmentSubmissionService.enrollStudentInCourse(student, course)).thenReturn(true);
	// 	boolean enrollmentSuccess = assignmentSubmissionService.enrollStudentInCourse(student, course);
	// 	assertTrue(enrollmentSuccess);
	// 	verify(assignmentSubmissionService, times(1)).enrollStudentInCourse(student, course);
	// }
	
}
