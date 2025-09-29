package com.marathicoder.controller;
 
 
 
import com.marathicoder.service.RemoveCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.Map;
 
@RestController
@RequestMapping("/remove-course")
@CrossOrigin(origins = "http://localhost:4200")
public class RemoveCourseController {
 
    @Autowired
    private RemoveCourseService service;
 
    @PostMapping
    public ResponseEntity<String> removeCourse(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String course = payload.get("course");
 
        if (email == null || course == null) {
            return ResponseEntity.badRequest().body("Email and course are required in JSON body");
        }
 
        String result = service.removeCourseFromTrainer(email, course);
        return ResponseEntity.ok(result);
    }
}
 
 