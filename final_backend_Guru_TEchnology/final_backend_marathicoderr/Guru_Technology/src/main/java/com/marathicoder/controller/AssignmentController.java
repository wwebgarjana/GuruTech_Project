


package com.marathicoder.controller;

import com.marathicoder.config.JwtUtil;
import com.marathicoder.model.Assignment;
import com.marathicoder.model.NotificationEntity;
import com.marathicoder.model.Student;
import com.marathicoder.model.StudentTrainerAssign;
import com.marathicoder.model.Trainer;
import com.marathicoder.repository.StudentRepository;
import com.marathicoder.repository.StudentTrainerAssignRepository;
import com.marathicoder.repository.TrainerRepository;
import com.marathicoder.service.AssignmentService;
import com.marathicoder.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assignments")
@CrossOrigin(origins = "http://localhost:4200")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentTrainerAssignRepository assignRepo;
    
    @Autowired
    private NotificationService notificationService;


    // ------------------ TRAINER ------------------

    
    
    



    
    
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadAssignment(
            @RequestHeader("Authorization") String token,
            @RequestParam("batch") String batch,
            @RequestParam("course") String course,
            @RequestParam("topic") String topic,
            @RequestParam("description") String description,
            @RequestParam("dueDate") String dueDate,
            @RequestParam("file") MultipartFile file) {

        Map<String, String> response = new HashMap<>();
        try {
            // ✅ Extract trainer email from token
            String email = jwtUtil.extractUsername(token.substring(7));
            Trainer trainer = trainerRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Trainer not found!"));
            String trainerId = trainer.getTrainerId();

            // ✅ Get all students assigned to this trainer
            List<StudentTrainerAssign> assignedStudents = assignRepo.findByTrainerId(trainerId);

            // ✅ Filter only those students who belong to the requested batch (multi-batch support)
            List<StudentTrainerAssign> batchStudents = assignedStudents.stream()
                .filter(s -> {
                    String batches = s.getBatches(); // e.g., "Batch A,Batch B"
                    return batches != null && Arrays.stream(batches.split(","))
                                                   .map(String::trim)
                                                   .anyMatch(b -> b.equalsIgnoreCase(batch));
                })
                .toList();

            if (batchStudents.isEmpty()) {
                response.put("message", "❌ No students found for batch: " + batch);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // ✅ Loop through each student: upload assignment + send notification
            for (StudentTrainerAssign assign : batchStudents) {
                // Upload assignment
                assignmentService.uploadAssignment(
                        topic,
                        description,
                        LocalDate.parse(dueDate),
                        file,
                        trainerId,
                        assign.getStudentId(),
                        assign.getStudentEmail(),
                        trainer.getEmail() 
                );

                // Send notification
                NotificationEntity n = new NotificationEntity();
                n.setSenderRole("TRAINER");
                n.setReceiverRole("STUDENT");
                n.setBatch(batch);
                n.setReceiverEmail(assign.getStudentEmail());
                n.setMessage("New assignment uploaded: " + topic + " for batch " + batch);
                notificationService.send(n);
            }

            response.put("message", "✅ Assignment uploaded & notifications sent for batch " + batch +
                    " (" + batchStudents.size() + " students)");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "❌ Upload failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // ------------------ STUDENT ------------------
    @GetMapping("/student")
    public ResponseEntity<?> getStudentAssignments(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.extractUsername(token.substring(7));
            Optional<Student> studentOpt = studentRepository.findByEmail(email);
            if (studentOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("❌ Student not found");
            }

            List<Assignment> assignments = assignmentService.getAssignmentsByStudent(studentOpt.get().getStudentId());
            List<Map<String, Object>> response = assignments.stream().map(a -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", a.getId());
                map.put("topic", a.getTopic());
                map.put("description", a.getDescription());
                map.put("dueDate", a.getDueDate());
                map.put("downloadUrl", "/api/assignments/download/" + a.getId());
                map.put("fileName", a.getFileName());
                return map;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Invalid token");
        }
    }

    // Download assignment
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadAssignment(@PathVariable Long id) {
        return assignmentService.getAssignmentById(id)
                .map(a -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    headers.setContentDisposition(ContentDisposition.attachment()
                            .filename(a.getFileName())
                            .build());
                    return new ResponseEntity<>(a.getFileData(), headers, HttpStatus.OK);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
