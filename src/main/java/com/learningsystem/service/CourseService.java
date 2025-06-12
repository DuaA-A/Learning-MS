package com.learningsystem.service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learningsystem.entity.Course;
import com.learningsystem.entity.CourseContent;
import com.learningsystem.entity.User;
import com.learningsystem.repository.CourseContentRepository;
import com.learningsystem.repository.CourseRepository;
import com.learningsystem.repository.UserRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CourseContentRepository courseContentRepository;

    public Course createCourse(String title, String description, String instructorEmail, LocalDate startDate, LocalDate endDate, Duration duration) {
        User instructor = userRepository.findByUsername(instructorEmail)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        course.setInstructor(instructor);
        course.setStartDate(startDate);
        course.setEndDate(endDate);
        course.setDuration(duration);
        return courseRepository.save(course);
    }

    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId).orElse(null);
    }
    

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getCoursesByInstructor(String username) {
        User instructor = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        return courseRepository.findByInstructor(instructor);
    }

    public Course getCourseDetails(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }
    
    public List<Course> getAvailableCoursesByStudentId(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return courseRepository.findAll().stream()
                .filter(course -> !course.getStudents().contains(student))
                .collect(Collectors.toList());
    }

    public Course updateCourse(Long courseId, Course courseRequest) {
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    
        existingCourse.setTitle(courseRequest.getTitle());
        existingCourse.setDescription(courseRequest.getDescription());
        existingCourse.setStartDate(courseRequest.getStartDate());
        existingCourse.setEndDate(courseRequest.getEndDate());
        existingCourse.setDuration(courseRequest.getDuration());
    
        return courseRepository.save(existingCourse);
    }
       public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }
    

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
    public List<User> getEnrolledStudents(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return List.copyOf(course.getStudents());
    }

    public void removeStudentFromCourse(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        course.getStudents().remove(student);
        courseRepository.save(course);
    }
    
    public List<Course> getAvailableCourses(long studentId) {
        return courseRepository.findAll().stream()
                .filter(course -> course.getStudents().stream()
                        .noneMatch(student -> student.getId().equals(studentId)))
                .collect(Collectors.toList());
    }
    
    public List<Course> getCoursesByStudent(String studentName) {
        return courseRepository.findAll().stream()
                .filter(course -> course.getStudents().stream()
                        .anyMatch(student -> student.getUsername().equals(studentName)))
                .collect(Collectors.toList());
    }
    
    public Course enrollStudent(Long courseId, String studentName) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        User student = userRepository.findByUsername(studentName)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        course.getStudents().add(student);
        return courseRepository.save(course);
    }
    
    public List<CourseContent> getCourseContent(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return courseContentRepository.findByCourse(course);
    }
}
