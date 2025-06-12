
package com.learningsystem.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.learningsystem.entity.Assignment;
import com.learningsystem.entity.AssignmentSubmission;
import com.learningsystem.entity.User;

@Repository
public class AssignmentSubmissionRepository {
    private final static Map<Long, AssignmentSubmission> submissionStore = new ConcurrentHashMap<>();
        private long currentId = 1;
    
        public AssignmentSubmission save(AssignmentSubmission submission) {
            if (submission.getId() == null) 
                submission.setId(currentId++);
            submissionStore.put(submission.getId(), submission);
            return submission;
        }
    
        public Optional<AssignmentSubmission> findById(Long id) {
            return Optional.ofNullable(submissionStore.get(id));
        }
    
        public List<AssignmentSubmission> findByStudent(User student) {
            List<AssignmentSubmission> submissions = new ArrayList<>();
            for (AssignmentSubmission submission : submissionStore.values()) 
                if (submission.getStudent().equals(student)) 
                    submissions.add(submission);
            return submissions;
        }
    
        public List<AssignmentSubmission> findByAssignment(Assignment assignment) {
            List<AssignmentSubmission> submissions = new ArrayList<>();
            for (AssignmentSubmission submission : submissionStore.values())
                if (submission.getAssignment().equals(assignment)) 
                    submissions.add(submission);
            return submissions;
        }
    
        public List<AssignmentSubmission> findAll(){
            return new ArrayList<>(submissionStore.values());
        }
    
        public void deleteById(Long id) {
            submissionStore.remove(id);
        }
    
        public List<AssignmentSubmission> findByStatus(String status) { 
            List<AssignmentSubmission> submissions = new ArrayList<>();
            for (AssignmentSubmission submission : submissionStore.values())
                if (submission.getStatus().equalsIgnoreCase(status))
                    submissions.add(submission);
            return submissions;
        }
    
        public static Optional<AssignmentSubmission> findByAssignmentIdAndStudentId(Long assignmentId, Long studentId) {
            return submissionStore.values().stream()
                .filter(submission -> submission.getAssignment().getId().equals(assignmentId) &&
                                    submission.getStudent().getId().equals(studentId))
                .findFirst();
    }
}
