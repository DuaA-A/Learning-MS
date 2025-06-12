// package com.learningsystem.controller;

// import com.learningsystem.entity.*;
// import com.learningsystem.repository.QuestionRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.security.access.prepost.PreAuthorize;

// import java.util.List;

// @RestController
// @RequestMapping("/api/instructor")
// public class InstructorQuestionController {

//     @Autowired
//     private QuestionRepository questionRepository;

//     // Add a new question to the question bank
//     @PreAuthorize("hasRole('INSTRUCTOR')")
//     @PostMapping("/questions")
//     public ResponseEntity<Question> addQuestion(@RequestBody Question question) {
//         Question savedQuestion = questionRepository.save(question);
//         return ResponseEntity.ok(savedQuestion);
//     }

//     // Get all questions (Question Bank)
//     @PreAuthorize("hasRole('INSTRUCTOR')")
//     @GetMapping("/questions")
//     public ResponseEntity<List<Question>> getAllQuestions() {
//         List<Question> questions = questionRepository.findAll();
//         return ResponseEntity.ok(questions);
//     }
// }