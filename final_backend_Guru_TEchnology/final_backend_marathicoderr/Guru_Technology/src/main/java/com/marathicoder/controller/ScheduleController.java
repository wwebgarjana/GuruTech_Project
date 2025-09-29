




package com.marathicoder.controller;

import com.marathicoder.config.JwtUtil;
import com.marathicoder.model.NotificationEntity;
import com.marathicoder.model.Schedule;
import com.marathicoder.model.StudentTrainerAssign;
import com.marathicoder.model.Trainer;
import com.marathicoder.model.Student;
import com.marathicoder.repository.ScheduleRepository;
import com.marathicoder.repository.StudentRepository;
import com.marathicoder.repository.StudentTrainerAssignRepository;
import com.marathicoder.repository.TrainerRepository;
import com.marathicoder.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = "http://localhost:4200")
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private StudentTrainerAssignRepository assignRepo;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private NotificationService notificationService;
    


    @PostMapping("/create")
    public ResponseEntity<?> createSchedule(
            @RequestHeader("Authorization") String token,
            @RequestBody Schedule scheduleRequest) {

        try {
            String email = jwtUtil.extractUsername(token.substring(7));
            Trainer trainer = trainerRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Trainer not found"));
            String trainerId = trainer.getTrainerId();

            List<StudentTrainerAssign> assignedStudents = assignRepo.findByTrainerId(trainerId)
                    .stream()
                    .filter(s -> scheduleRequest.getBatch().equalsIgnoreCase(s.getBatch()))
                    .toList();

            if (assignedStudents.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("❌ No students found in batch: " + scheduleRequest.getBatch());
            }

            for (StudentTrainerAssign assign : assignedStudents) {
                Schedule s = new Schedule();
                s.setBatch(assign.getBatch());
                s.setCourseName(scheduleRequest.getCourseName());
                s.setTopicName(scheduleRequest.getTopicName());
                s.setStartTime(scheduleRequest.getStartTime());
                s.setEndTime(scheduleRequest.getEndTime());
                s.setDate(scheduleRequest.getDate());
                s.setMeetLink(scheduleRequest.getMeetLink()); // ✅ add meet link from frontend

                s.setTrainerId(trainerId);
                s.setTrainerEmail(email);
                s.setStudentId(assign.getStudentId());
                s.setStudentEmail(assign.getStudentEmail());

                scheduleRepository.save(s);

                // Send notification to each student individually
                NotificationEntity n = new NotificationEntity();
                n.setSenderRole("TRAINER");
                n.setReceiverRole("STUDENT");
                n.setBatch(assign.getBatch());
                n.setReceiverEmail(assign.getStudentEmail()); // student-specific
                n.setMessage("New class scheduled: " + s.getTopicName() + " on " + s.getDate());
                notificationService.send(n);
            }

            return ResponseEntity.ok("✅ Schedule created & notifications sent for "
                    + assignedStudents.size() + " students in batch " + scheduleRequest.getBatch());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating schedule: " + e.getMessage());
        }
    }


    // ✅ Get schedules of trainer (JWT based)
    @GetMapping("/trainer")
    public ResponseEntity<?> getSchedulesByTrainer(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        Trainer trainer = trainerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Trainer not found for email: " + email));
        return ResponseEntity.ok(scheduleRepository.findByTrainerId(trainer.getTrainerId()));
    }



    // ✅ Get schedules by date
    @GetMapping("/date/{date}")
    public List<Schedule> getSchedulesByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return scheduleRepository.findByDate(localDate);
    }
    @GetMapping("/student/{studentId}")
    public List<Schedule> getSchedulesForStudent(@PathVariable String studentId) {
        return scheduleRepository.findByStudentId(studentId);
    }
    

    
 // ✅ Fetch upcoming schedules for a student by email
    @GetMapping("/student/upcoming/email/{email}")
    public List<Schedule> getUpcomingSchedulesForStudentByEmail(@PathVariable String email) {
        LocalDate today = LocalDate.now();
        return scheduleRepository.findByStudentEmailAndDateAfterOrDateEquals(email, today, today);
    }
    
    @GetMapping("/student/upcoming")
    public ResponseEntity<?> getUpcomingSchedulesForStudent(
            @RequestHeader("Authorization") String token) {

        try {
            String jwt = token.substring(7);
            String email = jwtUtil.extractUsername(jwt);

            Optional<Student> studentOpt = studentRepository.findByEmail(email);
            if (studentOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("❌ Student not found!");
            }

            Student student = studentOpt.get();

            // ✅ Find assigned trainers for this student
            List<StudentTrainerAssign> assigned = assignRepo.findByStudentId(student.getStudentId());
            if (assigned.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("❌ You are not assigned to any trainer, so no schedules available.");
            }

            LocalDate today = LocalDate.now();

            // ✅ Collect all schedules for all assigned trainers, filtered by batch
            List<Schedule> schedules = assigned.stream()
                    .flatMap(assign -> scheduleRepository.findByTrainerId(assign.getTrainerId()).stream()
                            .filter(s -> s.getBatch().equalsIgnoreCase(assign.getBatch()) &&
                                         (s.getDate().isAfter(today) || s.getDate().isEqual(today))))
                    .toList();

            return ResponseEntity.ok(schedules);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("❌ Invalid or expired token");
        }
    }

    
 



}

