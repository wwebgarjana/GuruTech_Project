


package com.marathicoder.repository;

import com.marathicoder.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findBySubmissionDataIsNotNull();
    List<Project> findByTrainerId(String trainerId);
    List<Project> findByStudentId(String studentId);
    List<Project> findByStudentEmail(String studentEmail);
}
