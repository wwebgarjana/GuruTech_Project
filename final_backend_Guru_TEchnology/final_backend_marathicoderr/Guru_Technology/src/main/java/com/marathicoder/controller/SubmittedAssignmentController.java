



package com.marathicoder.controller;

import com.marathicoder.config.JwtUtil;
import com.marathicoder.model.Assignment;
import com.marathicoder.model.NotificationEntity;
import com.marathicoder.model.Student;
import com.marathicoder.model.StudentTrainerAssign;
import com.marathicoder.model.SubmittedAssignment;
import com.marathicoder.service.AssignmentService;
import com.marathicoder.service.NotificationService;
import com.marathicoder.service.StudentService;
import com.marathicoder.service.StudentTrainerAssignService;
import com.marathicoder.service.SubmittedAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/submitted-assignments")
@CrossOrigin(origins = "http://localhost:4200")
public class SubmittedAssignmentController {

    @Autowired
    private SubmittedAssignmentService submittedService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private StudentService studentService; // ✅ added

    @Autowired
    private JwtUtil jwtUtil; 
    
    @Autowired
    private StudentTrainerAssignService studentTrainerAssignService;
    
    @Autowired
    private NotificationService notificationService;// ✅ added

   
    
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitAssignment(
            @RequestParam("assignmentId") Long assignmentId,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        try {
            // 1️⃣ Extract student info from JWT
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.put("message", "❌ Unauthorized: No token found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String token = authHeader.substring(7);
            String email = jwtUtil.extractUsername(token);
            Optional<Student> studentOpt = studentService.findByEmail(email);
            if (studentOpt.isEmpty()) {
                response.put("message", "❌ Student not found");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            Student student = studentOpt.get();

            // 2️⃣ Get assignment details
            Optional<Assignment> assignOpt = assignmentService.getAssignmentById(assignmentId);
            if (assignOpt.isEmpty()) {
                response.put("message", "❌ Assignment not found");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            Assignment assignment = assignOpt.get();

            // 3️⃣ Save submission
            SubmittedAssignment submission = new SubmittedAssignment();
            submission.setAssignmentId(assignmentId);
            submission.setTopic(assignment.getTopic());
            submission.setStudentId(student.getStudentId());
            submission.setStudentEmail(student.getEmail());
            submission.setFileName(file.getOriginalFilename());
            submission.setFileData(file.getBytes());
            submission.setSubmittedDate(LocalDateTime.now());

            SubmittedAssignment saved = submittedService.save(submission);

            // 4️⃣ Send notification to the trainer
            NotificationEntity n = new NotificationEntity();
            n.setSenderRole("STUDENT");
            n.setReceiverRole("TRAINER");
            n.setBatch(assignment.getBatch()); // optional, if you want batch info
            n.setReceiverEmail(assignment.getTrainerEmail()); // trainer of the assignment
            n.setMessage("Assignment submitted: " + assignment.getTopic() + " by " + student.getName());
            notificationService.send(n);

            // 5️⃣ Return response
            response.put("id", saved.getId());
            response.put("assignmentId", saved.getAssignmentId());
            response.put("topic", saved.getTopic());
            response.put("studentId", saved.getStudentId());
            response.put("studentEmail", saved.getStudentEmail());
            response.put("fileName", saved.getFileName());
            response.put("fileUrl", "/api/submitted-assignments/download/" + saved.getId());

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("message", "❌ File upload failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    
    
    
    

    
    
    
   


    // --- Trainer views all submitted assignments ---
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllSubmitted() {
        List<SubmittedAssignment> all = submittedService.getAll();

        List<Map<String, Object>> list = all.stream().map(s -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", s.getId());
            map.put("assignmentId", s.getAssignmentId());

            // Fetch topic from assignment
            assignmentService.getAssignmentById(s.getAssignmentId())
                .ifPresent(a -> map.put("topic", a.getTopic()));

            // Show student info (now populated from JWT)
            map.put("studentId", s.getStudentId());
            map.put("studentEmail", s.getStudentEmail());

            map.put("fileName", s.getFileName());
            map.put("fileUrl", "/api/submitted-assignments/download/" + s.getId());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    // --- Download submitted assignment ---
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadSubmitted(@PathVariable Long id) {
        Optional<SubmittedAssignment> opt = submittedService.getById(id);
        if (opt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        SubmittedAssignment sub = opt.get();
        byte[] data = sub.getFileData();
        String fileName = sub.getFileName();

        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        if (mimeType == null) mimeType = "application/octet-stream";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(mimeType));
        headers.setContentDisposition(ContentDisposition.attachment().filename(fileName).build());

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
}

