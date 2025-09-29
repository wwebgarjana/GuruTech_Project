

package com.marathicoder.controller;

import com.marathicoder.config.JwtUtil;
import com.marathicoder.model.NotificationEntity;
import com.marathicoder.model.Project;
import com.marathicoder.model.Student;
import com.marathicoder.model.StudentTrainerAssign;
import com.marathicoder.model.Trainer;
import com.marathicoder.repository.StudentRepository;
import com.marathicoder.repository.StudentTrainerAssignRepository;
import com.marathicoder.repository.TrainerRepository;
import com.marathicoder.service.NotificationService;
import com.marathicoder.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:4200")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

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



    
    
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadProject(
            @RequestHeader("Authorization") String token,
            @RequestParam("batch") String batch,
            @RequestParam("course") String course,
            @RequestParam("description") String description,
            @RequestParam(value = "techStack", required = false) String techStack,
            @RequestParam("assignedDate") String assignedDate,
            @RequestParam("file") MultipartFile file
    ) {
        Map<String, String> response = new HashMap<>();
        try {
            // 1️⃣ Extract trainer email from JWT
            String email = jwtUtil.extractUsername(token.substring(7));
            Trainer trainer = trainerRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Trainer not found"));

            String trainerId = trainer.getTrainerId();

            // 2️⃣ Get all students assigned to this trainer
            List<StudentTrainerAssign> assignedStudents = assignRepo.findByTrainerId(trainerId);

            // 3️⃣ Filter only those students who are in the given batch
            List<StudentTrainerAssign> batchStudents = assignedStudents.stream()
                    .filter(s -> batch.equalsIgnoreCase(s.getBatch()))
                    .toList();

            if (batchStudents.isEmpty()) {
                response.put("message", "❌ No students found in batch: " + batch);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // 4️⃣ Loop through each student: assign project + send notification
            for (StudentTrainerAssign assign : batchStudents) {
                // Assign project
                projectService.assignProject(batch, course, description, techStack, assignedDate, file,
                        trainerId, assign.getStudentId(), assign.getStudentEmail(), trainer.getEmail() );

                // Send notification
                NotificationEntity n = new NotificationEntity();
                n.setSenderRole("TRAINER");
                n.setReceiverRole("STUDENT");
                n.setBatch(batch);
                n.setReceiverEmail(assign.getStudentEmail());
                n.setMessage("New project assigned: " + course + " for batch " + batch);
                notificationService.send(n);
            }

            response.put("message", "✅ Project assigned & notifications sent to " + batchStudents.size() + " students in batch " + batch);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "❌ Failed to assign project: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    
    
    // Fetch all projects for a student
    @GetMapping("/student")
    public ResponseEntity<?> getStudentProjects(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.extractUsername(token.substring(7));
            Student student = studentRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            List<Project> projects = projectService.getProjectsByStudent(student.getStudentId());
            List<Map<String, Object>> response = projects.stream().map(p -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", p.getId());
                map.put("batch", p.getBatch());
                map.put("course", p.getCourse());
                map.put("description", p.getDescription());
                map.put("status", p.getStatus());
                map.put("downloadUrl", "http://localhost:8082/api/projects/download/" + p.getId());
                return map;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Invalid or expired token");
        }
    }

    // Download assigned file
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadAssigned(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(p -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + p.getFileName() + "\"")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(p.getFileData()))
                .orElse(ResponseEntity.notFound().build());
    }

    // Submit project file

//    @PostMapping("/{id}/submit")
//    public ResponseEntity<?> submitProject(
//            @PathVariable Long id,
//            @RequestParam("submissionFile") MultipartFile submissionFile) {
//        try {
//            Project updated = projectService.submitProject(id, submissionFile);
//            if (updated == null) 
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
//            
//            return ResponseEntity.ok(updated);   // ✅ इथे response परत जातो
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Submission failed");
//        }
//    }
    
    
    
    @PostMapping("/{id}/submit")
    public ResponseEntity<?> submitProject(
            @PathVariable Long id,
            @RequestParam("submissionFile") MultipartFile submissionFile) {
        try {
            // 1️⃣ Submit the project
            Project updated = projectService.submitProject(id, submissionFile);
            if (updated == null) 
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");

            // 2️⃣ Send notification to trainer
            if (updated.getTrainerEmail() != null && !updated.getTrainerEmail().isEmpty()) {
                NotificationEntity n = new NotificationEntity();
                n.setSenderRole("STUDENT");
                n.setReceiverRole("TRAINER");
                n.setBatch(updated.getBatch());
                n.setReceiverEmail(updated.getTrainerEmail());
                n.setMessage("Project submitted: " + updated.getCourse() + " by " + updated.getStudentName()); // ✅ fixed
                System.out.println("Sending notification: " + n.getMessage());
                notificationService.send(n);
            }

            // 3️⃣ Return response
            return ResponseEntity.ok(updated);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Submission failed");
        }
    }


    // Download submission
    @GetMapping("/{id}/download/submission")
    public ResponseEntity<byte[]> downloadSubmission(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .filter(p -> p.getSubmissionData() != null)
                .map(p -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + p.getSubmissionFileName() + "\"")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(p.getSubmissionData()))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete project
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }


 // Fetch all submitted projects
    @GetMapping("/submitted/all")
    public ResponseEntity<List<Map<String, Object>>> getAllSubmittedProjects() {
        List<Project> projects = projectService.getAllSubmittedProjects();

        List<Map<String, Object>> response = projects.stream().map(p -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("batch", p.getBatch());
            map.put("course", p.getCourse());
            map.put("description", p.getDescription());
            map.put("status", p.getStatus());
            map.put("assignedDate", p.getAssignedDate());
            map.put("submissionFileName", p.getSubmissionFileName());

            // ✅ Student details add कर
            map.put("studentId", p.getStudentId());
            map.put("studentName", p.getStudentName());
            map.put("studentEmail", p.getStudentEmail());

            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

}
