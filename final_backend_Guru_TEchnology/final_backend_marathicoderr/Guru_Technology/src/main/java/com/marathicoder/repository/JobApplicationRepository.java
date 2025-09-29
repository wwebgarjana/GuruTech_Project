package com.marathicoder.repository;
 
 
 
import com.marathicoder.model.JobApplication;
import com.marathicoder.model.JobPost;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
 
import java.util.List;
 
@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
	 // Delete applications by JobPost entity
    void deleteByJobPost(JobPost jobPost);
 
    List<JobApplication> findByJobPostId(Long jobId);
 
    List<JobApplication> findByStudentEmail(String email);
    
   
 
}
 
 