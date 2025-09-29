package com.marathicoder.controller;
 
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import com.marathicoder.dto.EducationDetailsDTO;
import com.marathicoder.model.EducationDetails;
import com.marathicoder.service.EducationDetailsService;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/education")
public class EducationDetailsController {
 
    @Autowired
    private EducationDetailsService educationDetailsService;
 
    @PostMapping("/save")
    public ResponseEntity<String> saveEducation(@RequestBody EducationDetailsDTO dto) {
        if (dto.getEducationList() == null || dto.getEducationList().isEmpty()) {
            return ResponseEntity.badRequest().body("Education list is missing or empty");
        }
 
        educationDetailsService.saveEducationDetails(dto);
        return ResponseEntity.ok("Education details saved successfully");
    }
 
    @GetMapping("/get/{email}")
    public Map<String, Object> getaEducation(@PathVariable String email) {
        List<EducationDetails> list = educationDetailsService.getEducationByEmail(email);
        Map<String, Object> response = new HashMap<>();
        response.put("educationList", list);
        return response;
    }
 
    @GetMapping("/all")
    public List<EducationDetails> getAll() {
        return educationDetailsService.getAll();
    }
}
 