package com.marathicoder.repository;

import com.marathicoder.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByTrainerId(String trainerId);
    List<Schedule> findByStudentId(String studentId);
    List<Schedule> findByStudentEmail(String studentEmail);
    List<Schedule> findByDate(LocalDate date);
    List<Schedule> findByStudentIdAndDateAfterOrDateEquals(String studentId, LocalDate today, LocalDate today2);
	List<Schedule> findByStudentEmailAndDateAfterOrDateEquals(String email, LocalDate today, LocalDate today2);
	List<Schedule> findByStudentIdAndBatchAndDateAfterOrDateEquals(
		    String studentId, String batch, LocalDate after, LocalDate equal);

}
