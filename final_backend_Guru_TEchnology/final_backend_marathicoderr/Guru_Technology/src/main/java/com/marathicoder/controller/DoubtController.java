

package com.marathicoder.controller;

import com.marathicoder.model.Doubt;
import com.marathicoder.model.NotificationEntity;
import com.marathicoder.model.Student;
import com.marathicoder.model.StudentTrainerAssign;
import com.marathicoder.model.Trainer;
import com.marathicoder.repository.StudentRepository;
import com.marathicoder.repository.StudentTrainerAssignRepository;
import com.marathicoder.repository.TrainerRepository;
import com.marathicoder.service.DoubtService;
import com.marathicoder.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200")
public class DoubtController {

    private final DoubtService service;
    private final StudentTrainerAssignRepository assignRepo;
    private final StudentRepository studentRepo;
    private final TrainerRepository trainerRepo;
    
    @Autowired
    private NotificationService notificationService;

    public DoubtController(
            DoubtService service,
            StudentTrainerAssignRepository assignRepo,
            StudentRepository studentRepo,
            TrainerRepository trainerRepo
    ) {
        this.service = service;
        this.assignRepo = assignRepo;
        this.studentRepo = studentRepo;
        this.trainerRepo = trainerRepo;
    }

    // ───────────────────────────────
    // GET assigned trainers for a student (by email)
    @GetMapping("/assigned/trainers")
    public ResponseEntity<List<String>> getAssignedTrainers(@RequestParam String studentEmail) {
        Student student = studentRepo.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentEmail));

        List<StudentTrainerAssign> assigns = assignRepo.findByStudentId(student.getStudentId());

        List<String> trainerEmails = assigns.stream()
                .map(StudentTrainerAssign::getTrainerId)
                .map(id -> trainerRepo.findByTrainerId(id)
                        .map(Trainer::getEmail)
                        .orElse(id))
                .distinct()
                .collect(Collectors.toList());

        return ResponseEntity.ok(trainerEmails);
    }

    // GET assigned students for a trainer (by email)
    @GetMapping("/assigned/students")
    public ResponseEntity<List<String>> getAssignedStudents(@RequestParam String trainerEmail) {
        Trainer trainer = trainerRepo.findByEmail(trainerEmail)
                .orElseThrow(() -> new RuntimeException("Trainer not found: " + trainerEmail));

        List<StudentTrainerAssign> assigns = assignRepo.findByTrainerId(trainer.getTrainerId());

        List<String> studentEmails = assigns.stream()
                .map(StudentTrainerAssign::getStudentId) // get studentId
                .map(id -> {
                    Optional<Student> studentOpt = studentRepo.findByStudentId(id);
                    return studentOpt.map(Student::getEmail).orElse(id); // fallback to id if email missing
                })
                .distinct()
                .collect(Collectors.toList());


        return ResponseEntity.ok(studentEmails);
    }

    // ───────────────────────────────
    // Check if student-trainer pair is assigned
    private boolean isAssignedPair(String studentEmail, String trainerEmail) {
        Optional<Student> optStudent = studentRepo.findByEmail(studentEmail);
        Optional<Trainer> optTrainer = trainerRepo.findByEmail(trainerEmail);

        if (optStudent.isEmpty() || optTrainer.isEmpty()) return false;

        String studentId = optStudent.get().getStudentId();
        String trainerId = optTrainer.get().getTrainerId();

        List<StudentTrainerAssign> assigns = assignRepo.findByStudentId(studentId);
        return assigns.stream()
                .anyMatch(a -> trainerId.equals(a.getTrainerId()));
    }

    // ───────────────────────────────
    // Send a message (student -> trainer or trainer -> student)
    @PostMapping
    public ResponseEntity<?> postMessage(@RequestBody Doubt msg) {
        String studentEmail = "student".equalsIgnoreCase(msg.getSender()) ? msg.getSenderName() : msg.getReceiverName();
        String trainerEmail = "trainer".equalsIgnoreCase(msg.getSender()) ? msg.getSenderName() : msg.getReceiverName();

        if (!isAssignedPair(studentEmail, trainerEmail)) {
            return ResponseEntity.status(403).body("❌ Not allowed: student and trainer are not assigned.");
        }

        return ResponseEntity.ok(service.save(msg));
    }

  
    
    
    
    
    
    
    @GetMapping("/{studentEmail}/{trainerEmail}")
    public ResponseEntity<?> getConversation(@PathVariable String studentEmail,
                                             @PathVariable String trainerEmail) {
        if (!isAssignedPair(studentEmail, trainerEmail)) {
            return ResponseEntity.status(403).body(List.of());
        }

        List<Doubt> conversation = service.getConversation(studentEmail, trainerEmail);
        return ResponseEntity.ok(conversation);
    }

}
