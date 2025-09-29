


package com.marathicoder.controller;

import com.marathicoder.config.JwtUtil;
import com.marathicoder.model.Feedback;
import com.marathicoder.model.NotificationEntity;
import com.marathicoder.model.StudentTrainerAssign;
import com.marathicoder.model.Trainer;
import com.marathicoder.repository.StudentTrainerAssignRepository;
import com.marathicoder.repository.TrainerRepository;
import com.marathicoder.service.FeedbackService;
import com.marathicoder.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "http://localhost:4200")
public class FeedbackController {

    private final FeedbackService service;
    private final StudentTrainerAssignRepository assignRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TrainerRepository trainerRepository;
    
    @Autowired
    private NotificationService notificationService;

    public FeedbackController(FeedbackService service, StudentTrainerAssignRepository assignRepo) {
        this.service = service;
        this.assignRepo = assignRepo;
    }

    // ✅ Student submits feedback only for their assigned trainer
//    @PostMapping
//    public ResponseEntity<?> submitFeedback(@RequestHeader("Authorization") String token,
//                                            @RequestBody Feedback feedback) {
//        try {
//            String email = jwtUtil.extractUsername(token.substring(7));
//
//            // Check if student is assigned to a trainer
//            List<StudentTrainerAssign> assignments = assignRepo.findByStudentId(feedback.getStudentId());
//            if (assignments.isEmpty()) {
//                return ResponseEntity.badRequest().body("❌ You are not assigned to any trainer");
//            }
//
//            // Ensure the feedback is submitted only for the assigned trainer
//            StudentTrainerAssign assigned = assignments.get(0); // assuming one trainer per student
//            feedback.setTrainerId(assigned.getTrainerId());
//            feedback.setStudentEmail(email);
//
//            Feedback saved = service.save(feedback);
//            return ResponseEntity.ok(saved);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("❌ Failed to submit feedback");
//        }
//    }
    
    
    
    @PostMapping
    public ResponseEntity<?> submitFeedback(@RequestHeader("Authorization") String token,
                                            @RequestBody Feedback feedback) {
        try {
            String email = jwtUtil.extractUsername(token.substring(7));

            // Check if student is assigned to a trainer
            List<StudentTrainerAssign> assignments = assignRepo.findByStudentId(feedback.getStudentId());
            if (assignments.isEmpty()) {
                return ResponseEntity.badRequest().body("❌ You are not assigned to any trainer");
            }

            // Ensure the feedback is submitted only for the assigned trainer
            StudentTrainerAssign assigned = assignments.get(0); // assuming one trainer per student
            feedback.setTrainerId(assigned.getTrainerId());
            feedback.setStudentEmail(email);

            // Save feedback
            Feedback saved = service.save(feedback);

            // ✅ Send notification to admin
            NotificationEntity notification = new NotificationEntity();
            //notification.setTitle("New Feedback Submitted");
            notification.setMessage("Student " + feedback.getStudentName() + " submitted feedback for course " + feedback.getCourse() + " and batch " + feedback.getBatch());
            notification.setSenderRole("STUDENT");
            notification.setReceiverRole("ADMIN"); // assuming you have role-based notifications
            notification.setReceiverEmail("admin@gmail.com");
            notification.setReadStatus(false);
            notificationService.send(notification); // Inject NotificationService as a field

            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Failed to submit feedback");
        }
    }

    
    
    

    // ✅ Trainer fetches feedback only from their assigned students
    @GetMapping("/trainer/me")
    public ResponseEntity<?> getMyFeedback(@RequestHeader("Authorization") String token,
                                           @RequestParam(required = false) String course,
                                           @RequestParam(required = false) String batch) {
        try {
            String email = jwtUtil.extractUsername(token.substring(7));

            // Find trainer by email
            Trainer trainer = trainerRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Trainer not found"));

            String trainerId = trainer.getTrainerId();

            // Get students assigned to this trainer
            List<StudentTrainerAssign> assignedStudents = assignRepo.findByTrainerId(trainerId);
            List<String> studentIds = assignedStudents.stream()
                    .map(StudentTrainerAssign::getStudentId)
                    .collect(Collectors.toList());

            // Fetch feedback from only assigned students
            List<Feedback> feedbacks = service.findByCourseAndBatch(course, batch).stream()
                    .filter(f -> trainerId.equals(f.getTrainerId()) && studentIds.contains(f.getStudentId()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(feedbacks);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body("❌ Unauthorized or invalid token");
        }
    }
    @GetMapping
  public ResponseEntity<List<Feedback>> getFeedbacks(
          @RequestParam(required = false) String course,
          @RequestParam(required = false) String batch) {
      List<Feedback> list = service.findByCourseAndBatch(course, batch);
      return ResponseEntity.ok(list);
  }
}


