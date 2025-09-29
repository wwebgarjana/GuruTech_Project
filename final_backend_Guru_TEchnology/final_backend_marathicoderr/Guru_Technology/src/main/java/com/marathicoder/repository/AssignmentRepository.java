


package com.marathicoder.repository;

import com.marathicoder.model.Assignment;
import com.marathicoder.model.StudentTrainerAssign;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByTrainerId(String trainerId);
    List<Assignment> findByStudentId(String studentId);
    List<Assignment> findByStudentEmail(String studentEmail);
 // Wrong: findByTrainerIdAndBatchName(Long trainerId, String batchName);
    List<Assignment> findByTrainerIdAndBatch(String trainerId, String batch);



}
