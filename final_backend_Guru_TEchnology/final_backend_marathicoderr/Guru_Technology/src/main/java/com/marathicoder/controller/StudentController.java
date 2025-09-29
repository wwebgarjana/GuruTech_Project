package com.marathicoder.controller;
 
import com.marathicoder.model.NotificationEntity;
import com.marathicoder.model.Student;
import com.marathicoder.model.StudentTrainerAssign;
import com.marathicoder.model.UserEntity;
import com.marathicoder.repository.StudentRepository;
import com.marathicoder.repository.StudentTrainerAssignRepository;
import com.marathicoder.repository.UserRepository;
import com.marathicoder.service.NotificationService;
import com.marathicoder.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
 
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:4200")
public class StudentController {
 
    @Autowired
    private StudentRepository studentRepo;
 
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentTrainerAssignRepository studentTrainerAssignRepo;

 
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerStudent(@RequestBody Student student) {
        Map<String, String> response = new HashMap<>();
 
        if (studentRepo.existsByMobile(student.getMobile())) {
            response.put("error", "Mobile number already registered");
            return ResponseEntity.status(409).body(response);
        }
 
        if (studentRepo.existsByAadhaar(student.getAadhaar())) {
            response.put("error", "Aadhaar number already registered");
            return ResponseEntity.status(409).body(response);
        }
 
        studentRepo.save(student); // ✅ Step 1: Save student
 
        // ✅ Step 2: Save login credentials for login access
        UserEntity login = new UserEntity();
        login.setEmail(student.getEmail());
        login.setPassword(student.getPassword()); // (Note: For security, use BCrypt in production)
        login.setRole("student");
 
        userRepo.save(login);
 
        response.put("message", "Student registered and login access given");
        return ResponseEntity.ok(response);
    }
 
    @GetMapping("/profile")
    public ResponseEntity<?> getStudentProfile(@RequestParam String email) {
        String normalizedEmail = email.trim().toLowerCase();
        Optional<Student> studentOpt = studentRepo.findByEmail(normalizedEmail);
        if (studentOpt.isPresent()) {
            Student s = studentOpt.get();
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("studentId", s.getStudentId());
            data.put("collegeId", s.getCollegeId());
            data.put("name", s.getName());
            data.put("aadhaar", s.getAadhaar());
            data.put("mobile", s.getMobile());
            data.put("birthdate", s.getBirthdate());
            data.put("address", s.getAddress());
            data.put("course", s.getCourse());
            data.put("email", s.getEmail());
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(404).body("Student not found");
        }
    }
    @GetMapping("/all")
    public List<Map<String, Object>> getAllStudents() {
        List<Student> students = studentRepo.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
 
        for (Student s : students) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("studentId", s.getStudentId());
            data.put("collegeId", s.getCollegeId());
            data.put("name", s.getName());
            data.put("email", s.getEmail());
            data.put("mobile", s.getMobile());
            data.put("aadhaar", s.getAadhaar());
            data.put("birthdate", s.getBirthdate());
            data.put("address", s.getAddress());
            data.put("course", s.getCourse());
        
            result.add(data);
        }
 
        return result;
    }
    
    @GetMapping("/last-id")
    public String getLastStudentId() {
        return studentService.getLastStudentId();
    }
 
 ////////////////////////////////pooja new code//////////////////
    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable String studentId) {
        return studentRepo.findByStudentId(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
 
    // === Upload certificate for a student ===
//    @PostMapping("/upload-certificate/{studentId}")
//    public ResponseEntity<String> uploadCertificate(
//            @PathVariable String studentId,
//            @RequestParam("file") MultipartFile file) {
//        try {
//            Optional<Student> studentOpt = studentRepo.findByStudentId(studentId);
//            if (studentOpt.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("Student not found with ID: " + studentId);
//            }
// 
//            Student student = studentOpt.get();
//            student.setCertificateName(file.getOriginalFilename());
//            student.setUploadCertificate(file.getBytes());
// 
//            studentRepo.save(student);
// 
//            return ResponseEntity.ok("Certificate uploaded successfully!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error uploading certificate");
//        }
//    }
// 
 
    
    
    @Autowired
    private NotificationService notificationService;  // ✅ Add this


    // === Upload certificate for a student ===
    @PostMapping("/upload-certificate/{studentId}")
    public ResponseEntity<String> uploadCertificate(
            @PathVariable String studentId,
            @RequestParam("file") MultipartFile file) {
        try {
            Optional<Student> studentOpt = studentRepo.findByStudentId(studentId);
            if (studentOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Student not found with ID: " + studentId);
            }

            Student student = studentOpt.get();
            student.setCertificateName(file.getOriginalFilename());
            student.setUploadCertificate(file.getBytes());

            studentRepo.save(student);

            // ✅ Send notification to student
            NotificationEntity notification = new NotificationEntity();
            notification.setMessage("🎓 Certificate uploaded: " + file.getOriginalFilename());
            notification.setSenderRole("ADMIN");      // Or "SYSTEM"
            notification.setReceiverRole("STUDENT");
            notification.setReceiverEmail(student.getEmail());
            notification.setReadStatus(false);
            notificationService.send(notification);

            return ResponseEntity.ok("Certificate uploaded successfully and notification sent!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading certificate");
        }
    }

    
    
    
    
    
    @GetMapping("/download-certificate/{studentId}")
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable String studentId) {
        try {
            // Find student by studentId
            Optional<Student> studentOpt = studentRepo.findByStudentId(studentId);
            if (studentOpt.isEmpty() || studentOpt.get().getUploadCertificate() == null) {
                return ResponseEntity.notFound().build();
            }
 
            Student student = studentOpt.get();
 
            // Use the original uploaded filename or fallback
            String fileName = student.getCertificateName() != null
                              ? student.getCertificateName()
                              : student.getName() + "_certificate.pdf";
 
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM) // same as Note
                    .body(student.getUploadCertificate());
 
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
 
 
 
    // === List students with certificate info (summary) ===
    @GetMapping("/certificate-summary")
    public List<Map<String, Object>> getAllCertificatesSummary() {
        List<Student> students = studentRepo.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
 
        for (Student s : students) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("studentId", s.getStudentId());
            data.put("name", s.getName());
            data.put("email", s.getEmail());
            data.put("course", s.getCourse());
            data.put("certificateName", s.getCertificateName());
            data.put("hasCertificate", s.getUploadCertificate() != null);
            result.add(data);
        }
        return result;
    }
    
// Get certificates for a specific student by email
    @GetMapping("/my-certificates")
    public List<Map<String, Object>> getCertificatesByEmail(@RequestParam String email) {
        Optional<Student> studentOpt = studentRepo.findByEmail(email);
        if(studentOpt.isEmpty()) return Collections.emptyList();
 
        Student student = studentOpt.get();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("studentId", student.getStudentId());
        data.put("name", student.getName());
        data.put("email", student.getEmail());
        data.put("course", student.getCourse());
        data.put("certificateName", student.getCertificateName());
        data.put("hasCertificate", student.getUploadCertificate() != null);
 
        return Collections.singletonList(data);
    }
    @PostMapping("/upload-image/{email}")
    public ResponseEntity<String> uploadImage(
            @PathVariable String email,
            @RequestParam("file") MultipartFile file) throws IOException {
 
        Optional<Student> studentOpt = studentRepo.findByEmail(email);
        if (studentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
 
        Student student = studentOpt.get();
        student.setProfileImage(file.getBytes());
        studentRepo.save(student);
 
        return ResponseEntity.ok("Image uploaded successfully");
    }
 
    @GetMapping("/image/{email}")
    public ResponseEntity<byte[]> getImage(@PathVariable String email) {
        Optional<Student> studentOpt = studentRepo.findByEmail(email);
        if (studentOpt.isPresent() && studentOpt.get().getProfileImage() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(studentOpt.get().getProfileImage());
        }
        return ResponseEntity.notFound().build();
    }
 //////////////////////////////////////////////////////////////

    @GetMapping("/email/{email}/assignments")
    public ResponseEntity<?> getStudentAssignments(@PathVariable String email) {
        // Find student first
        Student student = studentRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Map<String, String>> assignments = new ArrayList<>();

        // Fetch all assignments from student_trainer_assign table
        List<StudentTrainerAssign> assignedList = studentTrainerAssignRepo.findByStudentEmail(email);

        for (StudentTrainerAssign assign : assignedList) {
            Map<String, String> data = new HashMap<>();
            data.put("batch", assign.getBatch());
            data.put("course", assign.getCourse());
            assignments.add(data);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("studentId", student.getStudentId());   // ✅ Added studentId
        response.put("studentName", student.getName());
        response.put("totalAssignments", assignments.size());
        response.put("assignments", assignments);

        return ResponseEntity.ok(response);
    }



    
}
 


