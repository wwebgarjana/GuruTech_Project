package com.marathicoder.controller;
 
import com.marathicoder.model.JobPost;
import com.marathicoder.model.NotificationEntity;
import com.marathicoder.model.Student;
import com.marathicoder.repository.StudentRepository;
import com.marathicoder.service.JobService;
import com.marathicoder.service.NotificationService;
import com.marathicoder.service.StudentService;
 
import java.util.Collections;
import java.util.List;
import java.util.Map;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:4200")
public class JobController {
 
    @Autowired
    private JobService jobService;
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private NotificationService notificationService;

    
    
    // POST a job
//    @PostMapping("/post")
//    public ResponseEntity<String> createJobPost(@RequestBody JobPost jobPost) {
//        String message = jobService.createJobPost(jobPost);
//        return ResponseEntity.ok(message);
//    }
    
    
    @PostMapping("/post")
    public ResponseEntity<String> createJobPost(@RequestBody JobPost jobPost) {
        String message = jobService.createJobPost(jobPost);

        // ✅ Send notifications to eligible students
        List<String> eligibleStudentEmails = jobService.getEligibleStudentEmails(jobPost.getCourse());
        for (String email : eligibleStudentEmails) {
            NotificationEntity notification = new NotificationEntity();
            notification.setMessage("New Job Posted: " + jobPost.getTitle() + " at " + jobPost.getCompanyName());
            notification.setSenderRole("ADMIN");
            notification.setReceiverRole("STUDENT");
            notification.setReceiverEmail(email);
            notification.setReadStatus(false);
            notificationService.send(notification);
        }

        return ResponseEntity.ok(message);
    }

    
    
 
    // GET all jobs
    @GetMapping("/all")
    public ResponseEntity<List<JobPost>> getAllJobs() {
        List<JobPost> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }
   
 
        // ✅ DELETE Job by ID
        @DeleteMapping("/delete/{id}")
        public ResponseEntity<String> deleteJob(@PathVariable Long id) {
            boolean deleted = jobService.deleteJob(id);
            if (deleted) {
                return ResponseEntity.ok("Job deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found");
            }
        }
        
 
        @GetMapping("/eligible-jobs/{email}")
        public ResponseEntity<List<JobPost>> getEligibleJobs(@PathVariable String email) {
            System.out.println("Fetching eligible jobs for: " + email);
            Student student = studentRepository.findByEmail(email).orElse(null);
 
            if (student == null) {
                System.out.println("❌ Student not found");
                return ResponseEntity.ok(Collections.emptyList());
            }
 
            System.out.println("✅ Student found: " + student.getName() + " | Course: " + student.getCourse());
 
            List<JobPost> jobs = jobService.getEligibleJobs(email);
            System.out.println("Jobs matched: " + jobs.size());
 
            return ResponseEntity.ok(jobs);
        }
 
 
//        @PostMapping("/apply/{jobId}")
//        public ResponseEntity<String> applyJob(
//                @PathVariable Long jobId,
//                @RequestBody Map<String, String> body) {
// 
//            String studentEmail = body.get("email");
//            try {
//                jobService.applyJob(jobId, studentEmail);
//                return ResponseEntity.ok("Applied successfully!");
//            } catch (RuntimeException e) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//            }
//        }
        
        
        @PostMapping("/apply/{jobId}")
        public ResponseEntity<String> applyJob(
                @PathVariable Long jobId,
                @RequestBody Map<String, String> body) {

            String studentEmail = body.get("email");

            try {
                jobService.applyJob(jobId, studentEmail);

                // ✅ Send notification to Admin
                NotificationEntity notification = new NotificationEntity();
                notification.setMessage("📢 Student " + studentEmail + " applied for Job ID: " + jobId);
                notification.setSenderRole("STUDENT");
                notification.setReceiverRole("ADMIN");
                notification.setReceiverEmail("admin@gmail.com"); // or keep null if all admins see
                notification.setReadStatus(false);

                notificationService.send(notification);

                return ResponseEntity.ok("Applied successfully!");
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }

        
        
 
      
        //admin ko show hoga kisne kisne apply kiya hein
        
        @GetMapping("/applications-report")
        public ResponseEntity<List<Map<String, Object>>> getApplicationsReport() {
            List<Map<String, Object>> report = jobService.getAllApplications();
            return ResponseEntity.ok(report);
        }
 
     // GET all applied jobs for a student
//        @GetMapping("/applied/{email}")
//        public ResponseEntity<List<Long>> getAppliedJobs(@PathVariable String email) {
//            List<Long> appliedJobIds = jobService.getAppliedJobIds(email);
//            return ResponseEntity.ok(appliedJobIds);
//        }
     // GET all applied jobs for a student
        @GetMapping("/applied/{email}")
        public ResponseEntity<List<Long>> getAppliedJobs(@PathVariable String email) {
            List<Long> appliedJobIds = jobService.getAppliedJobIds(email);
            return ResponseEntity.ok(appliedJobIds);
        }
 
 
    }
 
 
 
 