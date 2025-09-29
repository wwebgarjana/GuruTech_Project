package com.marathicoder.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marathicoder.model.SubmittedAssignment;

public interface SubmittedAssignmentRepository extends JpaRepository<SubmittedAssignment, Long> {

	List<SubmittedAssignment> findByAssignmentId(Long assignmentId);
}
