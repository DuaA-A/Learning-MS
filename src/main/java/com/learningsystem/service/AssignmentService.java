package com.learningsystem.service;

import com.learningsystem.entity.Assignment;
import com.learningsystem.entity.Course;
import com.learningsystem.entity.Grade;
import com.learningsystem.repository.AssignmentRepository;
import com.learningsystem.repository.CourseRepository;
import com.learningsystem.repository.GradeRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AssignmentService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    private final Map<Long, Assignment> assignments = new HashMap<>();
    private final Map<Long, Course> courses;
    private final AtomicLong assignmentIdGenerator = new AtomicLong(1);

    public AssignmentService(Map<Long, Course> courses) {
        this.courses = courses;
    }

    public Assignment createAssignment(Long courseId, String title, String description, String dueDate) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Assignment assignment = new Assignment();
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setDueDate(LocalDate.parse(dueDate));
        assignment.setCourse(course);
        return assignmentRepository.save(assignment);
    }

    public List<Assignment> getAssignmentsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return assignmentRepository.findAll().values().stream()
                .filter(assignment -> assignment.getCourse().equals(course))
                .toList();
    }

    public Assignment getAssignmentById(Long assignmentId) {
        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
    }

    public List<Assignment> getAssignmentsForCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return assignmentRepository.findByCourseId(course.getId());
    }

    public void deleteAssignment(Long assignmentId) {
        if (assignmentRepository.findById(assignmentId).isEmpty()) {
            throw new RuntimeException("Assignment not found");
        }
        assignmentRepository.deleteById(assignmentId);
    }

    public List<Grade> getAssignmentGradesForStudentInCourse(Long courseId, Long studentId) {
        return gradeRepository.findByCourseIdAndStudentIdAndType(courseId, studentId, "ASSIGNMENT");
    }
}
