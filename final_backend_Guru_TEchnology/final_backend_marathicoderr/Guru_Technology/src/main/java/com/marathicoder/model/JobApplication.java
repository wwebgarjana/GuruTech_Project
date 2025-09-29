package com.marathicoder.model;
 
 
 
import java.time.LocalDateTime;
import javax.persistence.*;
 
@Entity
@Table(name = "job_applications")
public class JobApplication {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private JobPost jobPost;
 
    @Column(nullable = false)
    private String studentEmail; // store student's email
 
    @Column(nullable = false)
    private LocalDateTime appliedDate = LocalDateTime.now();
 
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
 
    public JobPost getJobPost() { return jobPost; }
    public void setJobPost(JobPost jobPost) { this.jobPost = jobPost; }
 
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
 
    public LocalDateTime getAppliedDate() { return appliedDate; }
    public void setAppliedDate(LocalDateTime appliedDate) { this.appliedDate = appliedDate; }
}
 
 