package com.learningsystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learningsystem.entity.Assignment;
import com.learningsystem.entity.Attendance;
import com.learningsystem.entity.Lesson;
import com.learningsystem.entity.User;
import com.learningsystem.repository.AssignmentRepository;
import com.learningsystem.repository.AttendanceRepository;
import com.learningsystem.repository.LessonRepository;
import com.learningsystem.repository.UserRepository;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    public String generateOtpForLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        String otp = String.valueOf(new Random().nextInt(9000) + 1000);
        lesson.setOtp(otp);
        lessonRepository.save(lesson); 

        return otp;
    }

    public Attendance markAttendance(Long lessonId, String studentName, String otp) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        if (!otp.equals(lesson.getOtp())) 
            throw new RuntimeException("Invalid OTP");
        User student = userRepository.findByUsername(studentName)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Attendance attendance = new Attendance();
        attendance.setLesson(lesson);
        attendance.setStudent(student);
        attendance.setAttendedAt(LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceForStudentInCourse(Long courseId, Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        List<Attendance> attendanceRecords = attendanceRepository.findByStudentAndLessonCourseId(student, courseId);
        if (attendanceRecords.isEmpty()) 
            throw new RuntimeException("No attendance records found for this student in the course");
        return attendanceRecords;
    }

    public Assignment getAssignmentById(Long assignmentId) {
        return AssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
    }
    
    public List<Attendance> getAttendanceRecordsForStudent(Long studentId) {
        User student = UserRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        List<Attendance> allAttendanceRecords = (List<Attendance>) AttendanceRepository.findAll();
        return allAttendanceRecords.stream()
                .filter(record -> record.getStudent().equals(student))
                .collect(Collectors.toList());
    }
    // public List<Attendance> getAttendanceRecordsForStudent(Long studentId) {
    //     return AttendanceRepository.findByStudentId(studentId);
    // }
    


}
