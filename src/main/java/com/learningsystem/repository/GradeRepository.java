package com.learningsystem.repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.learningsystem.entity.Assignment;
import com.learningsystem.entity.Grade;
import com.learningsystem.entity.Quiz;
import com.learningsystem.entity.User;

@Repository
public class GradeRepository {
    private final static Map<Long, Grade> gradeStore = new ConcurrentHashMap<>();
        private static Long gradeIdCounter = 1L;
        
            public static Grade save(Grade grade) {
                if (grade.getId() == null) {
                    grade.setId(gradeIdCounter++);
            }
            gradeStore.put(grade.getId(), grade);
        return grade;
    }

    public Optional<Grade> findById(Long id) {
        return Optional.ofNullable(gradeStore.get(id));
    }

    public List<Grade> findByStudent(User student) {
        List<Grade> grades = new ArrayList<>();
        for (Grade grade : gradeStore.values()) {
            if (grade.getStudent().equals(student)) {
                grades.add(grade);
            }
        }
        return grades;
    }

    public List<Grade> findByAssignment(Assignment assignment) {
        List<Grade> grades = new ArrayList<>();
        for (Grade grade : gradeStore.values()) {
            if (grade.getType() != null && grade.getType().equals(assignment)) {
                grades.add(grade);
            }
        }
        return grades;
    }

    public List<Grade> findByQuiz(Quiz quiz) {
        List<Grade> grades = new ArrayList<>();
        for (Grade grade : gradeStore.values()) {
            if (grade.getType() != null && grade.getType().equals(quiz)) {
                grades.add(grade);
            }
        }
        return grades;
    }

    public List<Grade> findByCourseIdAndStudentIdAndType(Long courseId, Long studentId, String type) {
        List<Grade> grades = new ArrayList<>();
        for (Grade grade : gradeStore.values()) {
            if (grade.getCourse().getId().equals(courseId) &&
                grade.getStudent().getId().equals(studentId) &&
                grade.getType().equalsIgnoreCase(type)) {
                grades.add(grade);
            }
        }
        return grades;
    }

    public Map<Long, Grade> findAll() {
        return gradeStore;
    }

    public void deleteById(Long id) {
        gradeStore.remove(id);
    }
}
