package com.learningsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learningsystem.entity.Assignment;
import com.learningsystem.entity.AssignmentSubmission;
import com.learningsystem.entity.User;
import com.learningsystem.repository.AssignmentRepository;
import com.learningsystem.repository.AssignmentSubmissionRepository;
import com.learningsystem.repository.UserRepository;
@Service
public class AssignmentSubmissionService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssignmentSubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    public AssignmentSubmission submitAssignment(Long studentId, Long assignmentId, String content) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setStudent(student);
        submission.setAssignment(assignment);
        submission.setContent(content);
        submission.setStatus("Submitted");
        return submissionRepository.save(submission);
    }

    public List<AssignmentSubmission> getSubmissionsByStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return submissionRepository.findByStudent(student);
    }

    public List<AssignmentSubmission> getSubmissionsByAssignment(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        return submissionRepository.findByAssignment(assignment);
    }

    public AssignmentSubmission gradeAssignment(Long submissionId, int grade, String feedback) {
        AssignmentSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        submission.setGrade(grade);
        submission.setFeedback(feedback);
        submission.setStatus("Graded");
        return submissionRepository.save(submission);
    }

    public List<AssignmentSubmission> findGradedSubmissionsByStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return submissionRepository.findByStudent(student).stream()
                .filter(submission -> "Graded".equals(submission.getStatus()))
                .toList();
    }

    public static AssignmentSubmission getSubmissionByAssignmentAndStudent(Long assignmentId, Long studentId) {
        return AssignmentSubmissionRepository.findByAssignmentIdAndStudentId(assignmentId, studentId)
                .orElse(null);
    }
    
}
