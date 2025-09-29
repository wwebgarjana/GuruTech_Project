package com.marathicoder.service;
 
import com.marathicoder.model.JobApplication;
import com.marathicoder.model.JobPost;
import com.marathicoder.model.Student;
import com.marathicoder.repository.JobApplicationRepository;
import com.marathicoder.repository.JobPostRepository;
import com.marathicoder.repository.StudentRepository;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
 
import javax.transaction.Transactional;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class JobService {
 
	
	   @Autowired
	    private JobApplicationRepository jobApplicationRepository;
 
	
    @Autowired
    private JobPostRepository jobPostRepository;
    @Autowired
    private StudentRepository studentRepository;
 
    public String createJobPost(JobPost jobPost) {
        jobPostRepository.save(jobPost);
        return "Job posted successfully!";
    }
 
    public List<JobPost> getAllJobs() {
        return jobPostRepository.findAll(); // fetch all jobs
    }
 
 
    @Transactional
    public boolean deleteJob(Long jobId) {
        JobPost job = jobPostRepository.findById(jobId).orElse(null);
        if (job == null) return false;
 
        // Delete all applications for this job
        jobApplicationRepository.deleteByJobPost(job);
 
        // Delete the job itself
        jobPostRepository.delete(job);
 
        return true;
    }

 
 
    public List<JobPost> getEligibleJobs(String email) {
        Student student = studentRepository.findByEmailIgnoreCase(email).orElse(null);
 
        if (student == null || student.getCourse() == null) {
            System.out.println("❌ Student not found or course null for email: " + email);
            return Collections.emptyList();
        }
 
        String studentCourse = student.getCourse().trim().toLowerCase();
        System.out.println("✅ Student course: " + studentCourse);
 
        List<JobPost> allJobs = jobPostRepository.findAll();
        System.out.println("📌 Total jobs in DB: " + allJobs.size());
 
        List<JobPost> eligibleJobs = allJobs.stream()
            .filter(job -> {
                if (job.getCourse() == null) {
                    System.out.println("⚠️ Job " + job.getTitle() + " has NULL course");
                    return false;
                }
                String jobCourse = job.getCourse().trim().toLowerCase();
                boolean match = jobCourse.contains(studentCourse);
                System.out.println("🔍 Checking Job: " + job.getTitle() + " | JobCourse: " + jobCourse + " | Match: " + match);
                return match;
            })
            .collect(Collectors.toList());
 
        System.out.println("✅ Eligible Jobs Found: " + eligibleJobs.size());
        return eligibleJobs;
    }
 
  
    public List<Map<String, Object>> getAllApplications() {
        List<JobApplication> applications = jobApplicationRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
 
        for (JobApplication app : applications) {
            Map<String, Object> map = new HashMap<>();
            map.put("studentEmail", app.getStudentEmail());
 
            // fetch student info safely
            studentRepository.findByEmail(app.getStudentEmail()).ifPresent(student -> {
                map.put("studentName", student.getName());
                map.put("studentCourse", student.getCourse());
                map.put("studentMobile", student.getMobile());
            });
 
            // fetch job info safely
            JobPost job = app.getJobPost();
            if (job != null) {
                map.put("jobId", job.getId());
                map.put("jobTitle", job.getTitle());
                map.put("companyName", job.getCompanyName());
            }
 
            map.put("appliedDate", app.getAppliedDate());
 
            result.add(map);
        }
 
        return result;
    }
 
 
    public void applyJob(Long jobId, String studentEmail) {
        JobPost job = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
 
        // Optional: check if student already applied
        boolean alreadyApplied = jobApplicationRepository
                .findByJobPostId(jobId)
                .stream()
                .anyMatch(app -> app.getStudentEmail().equals(studentEmail));
 
        if (alreadyApplied) {
            throw new RuntimeException("You have already applied for this job");
        }
 
        JobApplication application = new JobApplication();
        application.setJobPost(job);
        application.setStudentEmail(studentEmail);
 
        jobApplicationRepository.save(application);
    }
 
    public List<Long> getAppliedJobIds(String email) {
        return jobApplicationRepository.findByStudentEmail(email)
                .stream()
                .map(app -> app.getJobPost().getId())
                .toList();
    }
 
    
    
    
    public List<String> getEligibleStudentEmails(String course) {
        if (course == null) return Collections.emptyList();

        return studentRepository.findAll().stream()
                .filter(s -> s.getCourse() != null && s.getCourse().equalsIgnoreCase(course))
                .map(Student::getEmail)
                .collect(Collectors.toList());
    }

   
}
 
 
 