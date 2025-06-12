package com.learningsystem.repository;

import com.learningsystem.entity.Attendance;
import com.learningsystem.entity.Lesson;
import com.learningsystem.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class AttendanceRepository {
    private final static Map<Long, Attendance> attendanceStore = new ConcurrentHashMap<>();
        private Long attendanceIdCounter = 1L;
    
        public Attendance save(Attendance attendance) {
            if (attendance.getId() == null) {
                attendance.setId(attendanceIdCounter++);
            }
            attendanceStore.put(attendance.getId(), attendance);
            return attendance;
        }
    
        public Optional<Attendance> findById(Long id) {
            return Optional.ofNullable(attendanceStore.get(id));
        }
    
        public List<Attendance> findByLesson(Lesson lesson) {
            List<Attendance> attendances = new ArrayList<>();
            for (Attendance attendance : attendanceStore.values()) {
                if (attendance.getLesson().equals(lesson)) {
                    attendances.add(attendance);
                }
            }
            return attendances;
        }
    
        // Find all attendance records for a specific student
        public List<Attendance> findByStudent(User student) {
            List<Attendance> attendances = new ArrayList<>();
            for (Attendance attendance : attendanceStore.values()) {
                if (attendance.getStudent().equals(student)) {
                    attendances.add(attendance);
                }
            }
            return attendances;
        }
    
        public static Map<Long, Attendance> findAll() {
            return attendanceStore;
    }

    public void deleteById(Long id) {
        attendanceStore.remove(id);
    }

    public List<Attendance> findByStudentAndLessonCourseId(User student, Long courseId) {
        List<Attendance> attendances = new ArrayList<>();
        for (Attendance attendance : attendanceStore.values()) {
            Lesson lesson = attendance.getLesson();
            if (lesson.getCourse().getId().equals(courseId) && attendance.getStudent().equals(student)) {
                attendances.add(attendance);
            }
        }
        return attendances;
    }


}
