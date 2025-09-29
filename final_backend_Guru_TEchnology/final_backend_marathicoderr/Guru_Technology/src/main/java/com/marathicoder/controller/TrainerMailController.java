
package com.marathicoder.controller;




import com.marathicoder.dto.TrainerEmailRequest;
import com.marathicoder.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/trainers")
@CrossOrigin(origins = "http://localhost:4200")
public class TrainerMailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-trainer-email")
    public ResponseEntity<?> sendTrainerEmail(@RequestBody TrainerEmailRequest dto) {
        emailService.sendTrainerWelcomeEmail(
            dto.getEmail(),
            dto.getName(),
            dto.getTrainerId(),
            dto.getPassword()
        );

        return ResponseEntity.ok(Map.of("message", "Trainer email sent"));
    }
}