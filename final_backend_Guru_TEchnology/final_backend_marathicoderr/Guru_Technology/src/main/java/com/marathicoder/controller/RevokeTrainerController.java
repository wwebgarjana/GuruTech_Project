

package com.marathicoder.controller;

import com.marathicoder.service.RevokeTrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/revoke-trainer")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class RevokeTrainerController {

    @Autowired
    private RevokeTrainerService service;

    @PostMapping
    public ResponseEntity<String> revokeTrainerAccess(@RequestBody Map<String, String> payload) {
        String trainerId = payload.get("trainerId");
        String email = payload.get("email");

        if (trainerId == null || email == null) {
            return ResponseEntity.badRequest().body("❌ trainerId and email are required in JSON body");
        }

        String result = service.revokeAccess(trainerId, email);
        return ResponseEntity.ok(result);
    }
}
