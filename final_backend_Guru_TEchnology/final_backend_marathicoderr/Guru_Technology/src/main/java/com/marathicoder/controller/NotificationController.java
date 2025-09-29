package com.marathicoder.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.marathicoder.config.JwtUtil;
import com.marathicoder.model.NotificationEntity;
import com.marathicoder.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {

    private final NotificationService service;

    @Autowired
    private JwtUtil jwtUtil;
    
    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<NotificationEntity> create(@RequestBody NotificationEntity n) {
        return ResponseEntity.ok(service.send(n));
    }

//    @GetMapping("/trainer/{email}")
//    public ResponseEntity<List<NotificationEntity>> getTrainerNotifications(@PathVariable String email) {
//        return ResponseEntity.ok(service.getTrainerNotifications(email));
//    }
//    
    
    @GetMapping("/trainer/{email}")
    public ResponseEntity<List<NotificationEntity>> getTrainerNotifications(@PathVariable String email) {
        return ResponseEntity.ok(service.getTrainerNotifications(email));
    }
    
    @GetMapping("/admin/{email}")
    public ResponseEntity<List<NotificationEntity>> getAdminNotifications(@PathVariable String email) {
        return ResponseEntity.ok(service.getAdminNotifications(email));
    }

 // Trainer sees notifications
    

    @GetMapping("/student/{email}")
    public ResponseEntity<List<NotificationEntity>> getStudentNotifications(@PathVariable String email) {
        return ResponseEntity.ok(service.getStudentNotifications(email));
    }

    @PostMapping("/mark-read/{id}")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        NotificationEntity n = service.getById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        n.setReadStatus(true);
        service.send(n);
        return ResponseEntity.ok("Marked as read");
    }
}