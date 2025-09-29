



package com.marathicoder.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.marathicoder.config.JwtUtil;
import com.marathicoder.dto.LoginRequest;
import com.marathicoder.dto.LoginResponse;
import com.marathicoder.model.Student;
import com.marathicoder.model.UserEntity;
import com.marathicoder.repository.StudentRepository;
import com.marathicoder.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ LOGIN Endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail());

        if (user == null || !user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        // Include firstLogin in response
        return ResponseEntity.ok(new LoginResponse(token, user.getRole(), user.getFirstLogin()));
    }

    // ✅ STUDENT REGISTER Endpoint
    @PostMapping("/students/register")
    public ResponseEntity<String> registerStudent(@RequestBody Student student) {
        if (userRepository.findByEmail(student.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered. Please log in.");
        }

        studentRepository.save(student);

        UserEntity login = new UserEntity();
        login.setEmail(student.getEmail());
        login.setPassword(student.getPassword());
        login.setRole("student");
        login.setFirstLogin(true); // first login flag
        userRepository.save(login);

        return ResponseEntity.ok("Student registered and login created");
    }

    // ✅ RESET PASSWORD Endpoint
    @PutMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String newPassword = req.get("newPassword");

        UserEntity user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPassword(newPassword);   // optionally use BCrypt
            user.setFirstLogin(false);
            userRepository.save(user);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Password updated successfully");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}

