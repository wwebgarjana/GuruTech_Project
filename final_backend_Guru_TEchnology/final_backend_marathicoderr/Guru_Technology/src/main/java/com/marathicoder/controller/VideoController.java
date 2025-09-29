


package com.marathicoder.controller;

import com.marathicoder.config.JwtUtil;
import com.marathicoder.model.NotificationEntity;
import com.marathicoder.model.Student;
import com.marathicoder.model.StudentTrainerAssign;
import com.marathicoder.model.Trainer;
import com.marathicoder.model.Video;
import com.marathicoder.repository.StudentRepository;
import com.marathicoder.repository.StudentTrainerAssignRepository;
import com.marathicoder.repository.TrainerRepository;
import com.marathicoder.service.NotificationService;
import com.marathicoder.service.VideoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/videos")
@CrossOrigin(origins = "http://localhost:4200")
public class VideoController {

    @Autowired
    private VideoService videoService;

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

    // ✅ Upload video (trainer uploads, linked to assigned student)

    
//    @PostMapping("/upload")
//    public ResponseEntity<Map<String, String>> uploadVideo(
//            @RequestHeader("Authorization") String token,
//            @RequestParam("title") String title,
//            @RequestParam("description") String description,
//            @RequestParam("videoFile") MultipartFile file) {
//
//        Map<String, String> response = new HashMap<>();
//        try {
//            // 1️⃣ Extract trainer email from JWT
//            String email = jwtUtil.extractUsername(token.substring(7));
//            Trainer trainer = trainerRepository.findByEmail(email)
//                    .orElseThrow(() -> new RuntimeException("Trainer not found!"));
//
//            String trainerId = trainer.getTrainerId();
//
//            // 2️⃣ Get all assigned students
//            List<StudentTrainerAssign> assignedStudents = assignRepo.findByTrainerId(trainerId);
//            if (assignedStudents.isEmpty()) {
//                response.put("message", "❌ No students assigned to trainer");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//            }
//
//            // 3️⃣ Save video for each assigned student
//            for (StudentTrainerAssign assign : assignedStudents) {
//                videoService.uploadVideo(
//                        title,
//                        description,
//                        file,
//                        trainerId,
//                        assign.getStudentId(),
//                        assign.getStudentEmail()
//                );
//            }
//
//            response.put("message", "✅ Video uploaded successfully for " + assignedStudents.size() + " students");
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.put("message", "❌ Failed to upload video: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//
//    @PostMapping("/upload")
//    public ResponseEntity<Map<String, String>> uploadVideo(
//            @RequestHeader("Authorization") String token,
//            @RequestParam("title") String title,
//            @RequestParam("description") String description,
//            @RequestParam("videoFile") MultipartFile file,
//            @RequestParam("batch") String batch // ✅ Batch parameter added
//    ) {
//        Map<String, String> response = new HashMap<>();
//        try {
//            // 1️⃣ Extract trainer email from JWT
//            String email = jwtUtil.extractUsername(token.substring(7));
//            Trainer trainer = trainerRepository.findByEmail(email)
//                    .orElseThrow(() -> new RuntimeException("Trainer not found!"));
//
//            String trainerId = trainer.getTrainerId();
//
//            // 2️⃣ Get all assigned students
//            List<StudentTrainerAssign> assignedStudents = assignRepo.findByTrainerId(trainerId);
//
//            // 3️⃣ Filter only students from the selected batch
//            List<StudentTrainerAssign> batchStudents = assignedStudents.stream()
//                    .filter(s -> batch.equalsIgnoreCase(s.getBatch()))
//                    .toList();
//
//            if (batchStudents.isEmpty()) {
//                response.put("message", "❌ No students found in batch: " + batch);
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//            }
//
//            // 4️⃣ Upload video for each student in the batch
//            for (StudentTrainerAssign assign : batchStudents) {
//                videoService.uploadVideo(
//                        title,
//                        description,
//                        file,
//                        trainerId,
//                        assign.getStudentId(),
//                        assign.getStudentEmail()
//                );
//            }
//
//            response.put("message", "✅ Video uploaded successfully for " + batchStudents.size() + " students in batch " + batch);
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.put("message", "❌ Failed to upload video: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }

    
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadVideo(
            @RequestHeader("Authorization") String token,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("videoFile") MultipartFile file,
            @RequestParam("batch") String batch
    ) {
        Map<String, String> response = new HashMap<>();
        try {
            // ✅ Extract trainer email from JWT
            String email = jwtUtil.extractUsername(token.substring(7));
            Trainer trainer = trainerRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Trainer not found!"));
            String trainerId = trainer.getTrainerId();

            // ✅ Get all assigned students
            List<StudentTrainerAssign> assignedStudents = assignRepo.findByTrainerId(trainerId);

            // ✅ Filter only students from the selected batch
            List<StudentTrainerAssign> batchStudents = assignedStudents.stream()
                    .filter(s -> batch.equalsIgnoreCase(s.getBatch()))
                    .toList();

            if (batchStudents.isEmpty()) {
                response.put("message", "❌ No students found in batch: " + batch);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // ✅ Upload video for each student and send notification
            for (StudentTrainerAssign assign : batchStudents) {
                // 1️⃣ Save video for this student
                videoService.uploadVideo(
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
                n.setMessage("New video uploaded: " + title + " for batch " + assign.getBatch());
                notificationService.send(n);
            }

            response.put("message", "✅ Video uploaded successfully & notifications sent for "
                    + batchStudents.size() + " students in batch " + batch);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "❌ Failed to upload video: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    
    
    
    // ✅ Fetch all videos for the logged-in student
    @GetMapping("/student")
    public ResponseEntity<?> getStudentVideos(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.extractUsername(token.substring(7));

            Optional<Student> studentOpt = studentRepository.findByEmail(email);
            if (studentOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("❌ Student not found");
            }

            Student student = studentOpt.get();

            // Fetch videos by studentId
            List<Video> videos = videoService.getVideosByStudent(student.getStudentId());

            List<Map<String, Object>> response = videos.stream().map(video -> {
                Map<String, Object> videoInfo = new HashMap<>();
                videoInfo.put("id", video.getId());
                videoInfo.put("title", video.getTitle());
                videoInfo.put("description", video.getDescription());
                videoInfo.put("videoUrl", "http://localhost:8082/api/videos/play/" + video.getId());
                return videoInfo;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Invalid or expired token");
        }
    }

    // ✅ Download
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadVideo(@PathVariable Long id) {
        return videoService.getVideoById(id)
                .map(video -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    headers.setContentDisposition(ContentDisposition.attachment()
                            .filename(video.getFileName())
                            .build());
                    return new ResponseEntity<>(video.getData(), headers, HttpStatus.OK);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Play
    @GetMapping("/play/{id}")
    public void playVideo(@PathVariable Long id, HttpServletResponse response) throws IOException {
        videoService.getVideoById(id).ifPresent(video -> {
            try {
                response.setContentType("video/mp4");
                response.getOutputStream().write(video.getData());
                response.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

