package com.learningsystem.controller;

import com.learningsystem.entity.Grade;
import com.learningsystem.entity.Attendance;
import com.learningsystem.repository.GradeRepository;
import com.learningsystem.repository.AttendanceRepository;
import com.learningsystem.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadReport() throws IOException {
        List<Grade> grades = gradeRepository.findAll().values().stream().toList();
        List<Attendance> attendanceRecords = attendanceRepository.findAll().values().stream().toList();

        byte[] report = reportService.generatePerformanceReport(grades, attendanceRecords);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=performance-and-attendance-report.xlsx")
                .body(report);
    }

    @GetMapping("/chart/performance-trends")
    public ResponseEntity<byte[]> getPerformanceTrendsChart() throws IOException {
        List<Grade> grades = gradeRepository.findAll().values().stream().toList();
        byte[] chart = reportService.generatePerformanceTrendsChart(grades);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(chart);
    }

    @GetMapping("/chart")
    public ResponseEntity<byte[]> getPerformanceChart() throws IOException {
        List<Grade> grades = gradeRepository.findAll().values().stream().toList();
        byte[] chart = reportService.generatePerformanceChart(grades);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(chart);
    }

    // Bar chart for grades comparison
    @GetMapping("/chart/grades-bar")
    public ResponseEntity<byte[]> getGradesBarChart(@RequestParam String courseTitle) throws IOException {
        List<Grade> grades = gradeRepository.findAll().values().stream().toList();
        byte[] chart = reportService.generateGradesBarChart(grades, courseTitle);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(chart);
    }

    // Pie chart for grade distribution
    @GetMapping("/chart/grades-pie")
    public ResponseEntity<byte[]> getGradesPieChart(@RequestParam String courseTitle) throws IOException {
        List<Grade> grades = gradeRepository.findAll().values().stream().toList();
        byte[] chart = reportService.generateGradesPieChart(grades, courseTitle);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(chart);
    }

    // Line chart for attendance trends
    @GetMapping("/chart/attendance-line")
    public ResponseEntity<byte[]> getAttendanceLineChart(@RequestParam String studentName) throws IOException {
        List<Attendance> attendanceRecords = attendanceRepository.findAll().values().stream().toList();
        byte[] chart = reportService.generateAttendanceLineChart(attendanceRecords, studentName);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(chart);
    }
}


