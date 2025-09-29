


package com.marathicoder.repository;

import com.marathicoder.model.Assignment;
import com.marathicoder.model.StudentTrainerAssign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StudentTrainerAssignRepository extends JpaRepository<StudentTrainerAssign, Long> {

    List<StudentTrainerAssign> findByStudentId(String studentId);

    Optional<StudentTrainerAssign> findByStudentIdAndTrainerIdAndTrainerCourseAndBatches(
            String studentId, String trainerId, String trainerCourse, String batches
    );

    @Query("SELECT COUNT(DISTINCT s.studentId) FROM StudentTrainerAssign s WHERE s.trainerId = :trainerId")
    long countUniqueStudentsByTrainerId(String trainerId);

	List<StudentTrainerAssign> findByTrainerIdAndBatches(String trainerId, String batch);

	List<StudentTrainerAssign> findByTrainerId(String trainerId);
	List<StudentTrainerAssign> findByStudentEmail(String email);
	   List<StudentTrainerAssign> findByTrainerEmail(String trainerEmail);

//    List<StudentTrainerAssign> findByTrainerId(String trainerId);
//
//	List<StudentTrainerAssign> findByTrainerIdAndBatchesAndAssignedDate(String trainerId, String batch, String date);
//	
//
//
//	
//	
	    List<StudentTrainerAssign> findByTrainerIdAndBatch(String trainerId, String batch);
	

	

}

