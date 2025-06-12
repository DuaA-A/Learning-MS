package com.learningsystem.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.learningsystem.entity.Assignment;
import com.learningsystem.entity.Course;
@Repository
public class AssignmentRepository {
    private final static Map<Long, Assignment> assignmentStore = new ConcurrentHashMap<>();
    private Long assignmentIdCounter = 1L;

    public Assignment save(Assignment assignment) {
        if (assignment.getId() == null) {
            assignment.setId(assignmentIdCounter++);
        }
        assignmentStore.put(assignment.getId(), assignment);
        return assignment;
    }

    public static Optional<Assignment> findById(Long id) {
        return Optional.ofNullable(assignmentStore.get(id));
    }

    public List<Assignment> findByCourse(Course course) {
        List<Assignment> assignments = new ArrayList<>();
        for (Assignment assignment : assignmentStore.values()) {
            if (assignment.getCourse().equals(course)) {
                assignments.add(assignment);
            }
        }
        return assignments;
    }

    public List<Assignment> findByCourseId(Long courseId) {
        List<Assignment> assignments = new ArrayList<>();
        for (Assignment assignment : assignmentStore.values()) {
            if (assignment.getCourse() != null && assignment.getCourse().getId().equals(courseId)) {
                assignments.add(assignment);
            }
        }
        return assignments;
    }

    public Map<Long, Assignment> findAll() {
        return assignmentStore;
    }

    public void deleteById(Long id) {
        assignmentStore.remove(id);
    }
}
