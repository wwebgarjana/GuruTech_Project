package com.marathicoder.controller;


import com.marathicoder.dto.StudentEmailRequest;
import com.marathicoder.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:4200")
public class StudentMailController {

    @Autowired
    private EmailService studentMailService;

    @PostMapping("/send-student-email")
    public ResponseEntity<?> sendStudentMail(@RequestBody StudentEmailRequest dto) {
        studentMailService.sendStudentWelcomeEmail(
            dto.getEmail(),
            dto.getName(),
            dto.getStudentId(),
            dto.getPassword()
        );

        // ✅ Return JSON response instead of plain text
        return ResponseEntity.ok(Map.of("message", "Student email sent"));
    }
}