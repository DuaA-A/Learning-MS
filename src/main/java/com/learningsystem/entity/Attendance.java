package com.learningsystem.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.learningsystem.repository.AttendanceRepository;
import com.learningsystem.repository.UserRepository;

public class Attendance {

    private Long id; 
    private Lesson lesson; 
    private User student; 
    private LocalDateTime attendedAt; // attendance timestamp

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public LocalDateTime getAttendedAt() {
        return attendedAt;
    }

    public void setAttendedAt(LocalDateTime attendedAt) {
        this.attendedAt = attendedAt;
    }
    public List<Attendance> getAttendanceRecordsForStudent(Long studentId) {
        User student = UserRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return ((Collection<Attendance>) AttendanceRepository.findAll()).stream()
                .filter(attendance -> attendance.getStudent().equals(student))
                .collect(Collectors.toList());
    }
}
