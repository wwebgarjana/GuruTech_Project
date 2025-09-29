package com.marathicoder.repository;

import com.marathicoder.model.CourseBatch;
import com.marathicoder.model.Trainer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseBatchRepository extends JpaRepository<CourseBatch, Long> {
	   List<CourseBatch> findByTrainer(Trainer trainer);
	   Optional<CourseBatch> findByTrainerAndCourseNameAndBatchName(Trainer trainer, String courseName, String batchName);
	// CourseBatchRepository.java
	   Optional<CourseBatch> findByCourseNameAndBatchName(String courseName, String batchName);
	   Optional<CourseBatch> findByBatchName(String batchName);


}


