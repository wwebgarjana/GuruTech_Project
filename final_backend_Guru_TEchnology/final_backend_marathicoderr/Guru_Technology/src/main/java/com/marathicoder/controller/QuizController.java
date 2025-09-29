package com.marathicoder.controller;

import com.marathicoder.config.JwtUtil;
import com.marathicoder.model.*;
import com.marathicoder.repository.StudentRepository;
import com.marathicoder.repository.StudentTrainerAssignRepository;
import com.marathicoder.repository.TrainerRepository;
import com.marathicoder.service.NotificationService;
import com.marathicoder.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "http://localhost:4200")
public class QuizController {

    @Autowired private QuizService quizService;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private TrainerRepository trainerRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private StudentTrainerAssignRepository assignRepo;
    @Autowired private NotificationService notificationService;

    // ✅ Create quiz for batch
    @PostMapping("/create/batch")
    public ResponseEntity<Map<String, String>> createQuizForBatch(
            @RequestHeader("Authorization") String token,
            @RequestParam("batch") String batch,
            @RequestBody Quiz quizRequest) {

        Map<String, String> response = new HashMap<>();
        try {
            String email = jwtUtil.extractUsername(token.substring(7));
            Trainer trainer = trainerRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Trainer not found!"));

            String trainerId = trainer.getTrainerId();
            List<StudentTrainerAssign> batchStudents = assignRepo.findByTrainerId(trainerId)
                    .stream()
                    .filter(s -> batch.equalsIgnoreCase(s.getBatch()))
                    .toList();

            if (batchStudents.isEmpty()) {
                response.put("message", "❌ No students found in batch: " + batch);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            for (StudentTrainerAssign assign : batchStudents) {
                Quiz quiz = new Quiz();
                quiz.setQuizTitle(quizRequest.getQuizTitle());
                quiz.setQuizDescription(quizRequest.getQuizDescription());
                quiz.setTrainerId(trainerId);
                quiz.setStudentId(assign.getStudentId());
                quiz.setStudentEmail(assign.getStudentEmail());

                // ✅ Copy questions with marks
                List<Question> questionsCopy = quizRequest.getQuestions().stream()
                        .map(q -> new Question(q.getQuestionText(), q.getOptions(), q.getCorrectAnswer(), q.getMarks()))
                        .collect(Collectors.toList());
                quiz.setQuestions(questionsCopy);

                quizService.createQuiz(quiz);

                NotificationEntity n = new NotificationEntity();
                n.setSenderRole("TRAINER");
                n.setReceiverRole("STUDENT");
                n.setBatch(batch);
                n.setReceiverEmail(assign.getStudentEmail());
                n.setMessage("New quiz assigned: " + quiz.getQuizTitle() + " for batch " + batch);
                notificationService.send(n);
            }

            response.put("message", "✅ Quiz created & notifications sent for " + batchStudents.size() + " students in batch " + batch);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "❌ Failed to create quiz: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ✅ Get quizzes for student
    @GetMapping("/student")
    @Transactional
    public ResponseEntity<?> getStudentQuizzes(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.extractUsername(token.substring(7));
            Student student = studentRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Student not found!"));

            List<Quiz> quizzes = quizService.getQuizzesByStudent(student.getStudentId());
            if (quizzes.isEmpty()) {
                return ResponseEntity.ok(Collections.singletonMap("message", "No quizzes available"));
            }

            return ResponseEntity.ok(quizzes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    // ✅ Submit answers (optional)
    @PostMapping("/submit")
    public ResponseEntity<?> submitQuiz(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> payload) {
        System.out.println("Submitted payload: " + payload);
        return ResponseEntity.ok(Collections.singletonMap("message", "Submission saved (mock)"));
    }
}
