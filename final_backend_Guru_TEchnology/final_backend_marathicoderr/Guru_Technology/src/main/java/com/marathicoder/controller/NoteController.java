


package com.marathicoder.controller;

import com.marathicoder.config.JwtUtil;
import com.marathicoder.model.Note;
import com.marathicoder.model.NotificationEntity;
import com.marathicoder.model.Student;
import com.marathicoder.model.StudentTrainerAssign;
import com.marathicoder.model.Trainer;
import com.marathicoder.repository.NoteRepository;
import com.marathicoder.repository.StudentRepository;
import com.marathicoder.repository.StudentTrainerAssignRepository;
import com.marathicoder.repository.TrainerRepository;
import com.marathicoder.service.NoteService;
import com.marathicoder.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "http://localhost:4200")
public class NoteController {

    @Autowired
    private NoteService noteService;

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

    // ✅ Upload note (trainer uploads, linked to all assigned students)


    
//    @PostMapping("/upload")
//    public ResponseEntity<Map<String, String>> uploadNote(
//            @RequestHeader("Authorization") String token,
//            @RequestParam("batch") String batch,           // ✅ Add batch
//            @RequestParam("title") String title,
//            @RequestParam("description") String description,
//            @RequestParam("file") MultipartFile file) {
//
//        Map<String, String> response = new HashMap<>();
//        try {
//            // 1️⃣ Extract trainer email
//            String email = jwtUtil.extractUsername(token.substring(7));
//            Trainer trainer = trainerRepository.findByEmail(email)
//                    .orElseThrow(() -> new RuntimeException("Trainer not found!"));
//
//            String trainerId = trainer.getTrainerId();
//
//            // 2️⃣ Get all students assigned to this trainer
//            List<StudentTrainerAssign> assignedStudents = assignRepo.findByTrainerId(trainerId);
//
//            // 3️⃣ Filter only students in the given batch
//            List<StudentTrainerAssign> batchStudents = assignedStudents.stream()
//                    .filter(s -> batch.equalsIgnoreCase(s.getBatch()))
//                    .toList();
//
//            if (batchStudents.isEmpty()) {
//                response.put("message", "❌ No students found in batch: " + batch);
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//            }
//
//            // 4️⃣ Save note for each student in the batch
//            for (StudentTrainerAssign assign : batchStudents) {
//                noteService.uploadNote(
//                        title,
//                        description,
//                        file,
//                        trainerId,
//                        assign.getStudentId(),
//                        assign.getStudentEmail()
//                );
//            }
//
//            response.put("message", "✅ Note uploaded successfully for " + batchStudents.size() + " students in batch " + batch);
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.put("message", "❌ Failed to upload note: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
    
    
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadNote(
            @RequestHeader("Authorization") String token,
            @RequestParam("batch") String batch,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file) {

        Map<String, String> response = new HashMap<>();
        try {
            // ✅ Extract trainer email
            String email = jwtUtil.extractUsername(token.substring(7));
            Trainer trainer = trainerRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Trainer not found!"));
            String trainerId = trainer.getTrainerId();

            // ✅ Get all students assigned to this trainer
            List<StudentTrainerAssign> assignedStudents = assignRepo.findByTrainerId(trainerId);

            // ✅ Filter only students in the given batch
            List<StudentTrainerAssign> batchStudents = assignedStudents.stream()
                    .filter(s -> batch.equalsIgnoreCase(s.getBatch()))
                    .toList();

            if (batchStudents.isEmpty()) {
                response.put("message", "❌ No students found in batch: " + batch);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // ✅ Save note and send notification for each student
            for (StudentTrainerAssign assign : batchStudents) {
                // 1️⃣ Save the note
                noteService.uploadNote(
                        title,
                        description,
                        file,
                        trainerId,
                        assign.getStudentId(),
                        assign.getStudentEmail()
                );

                // 2️⃣ Send notification
                NotificationEntity n = new NotificationEntity();
                n.setSenderRole("TRAINER");
                n.setReceiverRole("STUDENT");
                n.setBatch(assign.getBatch());
                n.setReceiverEmail(assign.getStudentEmail());
                n.setMessage("New note uploaded: " + title + " for batch " + assign.getBatch());
                notificationService.send(n);
            }

            response.put("message", "✅ Note uploaded successfully & notifications sent for "
                    + batchStudents.size() + " students in batch " + batch);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "❌ Failed to upload note: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // ✅ Fetch all notes for logged-in student
    @GetMapping("/student")
    public ResponseEntity<?> getStudentNotes(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.extractUsername(token.substring(7));

            Optional<Student> studentOpt = studentRepository.findByEmail(email);
            if (studentOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("❌ Student not found");
            }

            Student student = studentOpt.get();

            List<Note> notes = noteService.getNotesByStudent(student.getStudentId());

            List<Map<String, Object>> response = notes.stream().map(note -> {
                Map<String, Object> noteInfo = new HashMap<>();
                noteInfo.put("id", note.getId());
                noteInfo.put("title", note.getTitle());
                noteInfo.put("description", note.getDescription());
                noteInfo.put("downloadUrl", "http://localhost:8082/api/notes/download/" + note.getId());
                return noteInfo;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Invalid or expired token");
        }
    }

    // ✅ Download note
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadNote(@PathVariable Long id) {
        return noteService.getNoteById(id)
                .map(note -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    headers.setContentDisposition(ContentDisposition.attachment()
                            .filename(note.getFileName())
                            .build());
                    return new ResponseEntity<>(note.getFileData(), headers, HttpStatus.OK);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
