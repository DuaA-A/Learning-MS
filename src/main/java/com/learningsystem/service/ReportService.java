package com.learningsystem.service;

import com.learningsystem.entity.Grade;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;
import com.learningsystem.entity.Attendance;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.jfree.data.general.DefaultPieDataset;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    public byte[] generatePerformanceReport(List<Grade> grades, List<Attendance> attendanceRecords) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Performance Analysis");

        // Add header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Student Name");
        header.createCell(1).setCellValue("Course");
        header.createCell(2).setCellValue("Grade Type");
        header.createCell(3).setCellValue("Score");
        header.createCell(4).setCellValue("Total Quizzes");
        header.createCell(5).setCellValue("Total Assignments");
        header.createCell(6).setCellValue("Quiz Average");
        header.createCell(7).setCellValue("Assignment Average");
        header.createCell(8).setCellValue("Total Score");

        // Process data by student
        int rowIndex = 1;
        Map<String, List<Grade>> gradesByStudent = grades.stream()
                .collect(Collectors.groupingBy(grade -> grade.getStudent().getUsername()));

        for (Map.Entry<String, List<Grade>> entry : gradesByStudent.entrySet()) {
            String studentName = entry.getKey();
            List<Grade> studentGrades = entry.getValue();

            // Separate grades into quizzes and assignments
            List<Grade> quizzes = studentGrades.stream().filter(g -> g.getType().equals("QUIZ")).toList();
            List<Grade> assignments = studentGrades.stream().filter(g -> g.getType().equals("ASSIGNMENT")).toList();

            // Calculate totals and averages
            int totalQuizzes = quizzes.size();
            int totalAssignments = assignments.size();
            double quizAverage = quizzes.stream().mapToDouble(Grade::getScore).average().orElse(0.0);
            double assignmentAverage = assignments.stream().mapToDouble(Grade::getScore).average().orElse(0.0);
            double totalScore = studentGrades.stream().mapToDouble(Grade::getScore).sum();

            // Add data rows
            for (Grade grade : studentGrades) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(studentName);
                row.createCell(1).setCellValue(grade.getCourse().getTitle());
                row.createCell(2).setCellValue(grade.getType());
                row.createCell(3).setCellValue(grade.getScore());
                row.createCell(4).setCellValue(totalQuizzes);
                row.createCell(5).setCellValue(totalAssignments);
                row.createCell(6).setCellValue(quizAverage);
                row.createCell(7).setCellValue(assignmentAverage);
                row.createCell(8).setCellValue(totalScore);
            }
        }

        // Write to byte array
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            workbook.close();
            return outputStream.toByteArray();
        }
    }

    public byte[] generatePerformanceTrendsChart(List<Grade> grades) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        grades.forEach(grade ->
                dataset.addValue(grade.getScore(), grade.getType(), grade.getStudent().getUsername())
        );

        JFreeChart chart = ChartFactory.createBarChart(
                "Performance Trends",
                "Student",
                "Score",
                dataset
        );

        return writeChartToByteArray(chart);
    }


    public byte[] generatePerformanceChart(List<Grade> grades) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Grade grade : grades) {
            dataset.addValue(grade.getScore(), grade.getType(), grade.getStudent().getUsername());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Performance Analytics", "Student", "Score", dataset
        );

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(outputStream, chart, 800, 600);
            return outputStream.toByteArray();
        }
    }

    // Generate a bar chart comparing grades across students for a specific course
    public byte[] generateGradesBarChart(List<Grade> grades, String courseTitle) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Filter grades by course and add to dataset
        grades.stream()
                .filter(grade -> grade.getCourse().getTitle().equals(courseTitle))
                .forEach(grade -> dataset.addValue(grade.getScore(), grade.getStudent().getUsername(), grade.getType()));

        JFreeChart chart = ChartFactory.createBarChart(
                "Grades Comparison - " + courseTitle,
                "Student",
                "Score",
                dataset
        );

        return writeChartToByteArray(chart);
    }

    // Generate a pie chart showing grade distribution for a specific course
    public byte[] generateGradesPieChart(List<Grade> grades, String courseTitle) throws IOException {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Aggregate grade distribution by type
        Map<String, Long> gradeDistribution = grades.stream()
                .filter(grade -> grade.getCourse().getTitle().equals(courseTitle))
                .collect(Collectors.groupingBy(Grade::getType, Collectors.counting()));

        // Add data to the dataset
        gradeDistribution.forEach(dataset::setValue);

        JFreeChart chart = ChartFactory.createPieChart(
                "Grade Distribution - " + courseTitle,
                dataset,
                true,
                true,
                false
        );

        return writeChartToByteArray(chart);
    }

    // Generate a line chart showing attendance trends for a specific student
    public byte[] generateAttendanceLineChart(List<Attendance> attendanceRecords, String studentName) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Filter attendance records by student and add to dataset
        attendanceRecords.stream()
                .filter(attendance -> attendance.getStudent().getUsername().equals(studentName))
                .forEach(attendance -> dataset.addValue(1, "Attendance", attendance.getAttendedAt().toLocalDate()));

        JFreeChart chart = ChartFactory.createLineChart(
                "Attendance Trends - " + studentName,
                "Date",
                "Attendance",
                dataset
        );

        return writeChartToByteArray(chart);
    }

    // Helper method to write chart to a byte array
    private byte[] writeChartToByteArray(JFreeChart chart) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(outputStream, chart, 800, 600);
            return outputStream.toByteArray();
        }
    }
}








