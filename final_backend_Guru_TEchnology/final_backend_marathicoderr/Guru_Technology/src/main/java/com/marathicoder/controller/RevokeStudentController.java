package com.marathicoder.controller;
 
 
import com.marathicoder.service.RevokeStudentService;
 
import java.util.Map;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
@RestController
@RequestMapping("/revoke-student")
@CrossOrigin(
	    originPatterns = {"*"}, // allows all origins with credentials
	    allowCredentials = "true"
	)
public class RevokeStudentController {
 
    @Autowired
    private RevokeStudentService service;
 
    @PostMapping
    public ResponseEntity<String> revokeStudentAccess(@RequestBody Map<String, String> request) {
        String studentId = request.get("studentId");
        String email = request.get("email");
 
        String result = service.revokeAccess(studentId, email);
        return ResponseEntity.ok(result);
    }
 
}
 
 