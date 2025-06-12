package com.learningsystem.controller;

import com.learningsystem.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class EmailController {

    @Autowired
    private EmailService notificationService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(
            @RequestParam String email,
            @RequestParam String subject,
            @RequestParam String message) {

        notificationService.sendEmail(email, subject, message);
        return ResponseEntity.ok("Notification sent successfully!");
    }
}