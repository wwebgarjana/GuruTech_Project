

package com.marathicoder.repository;
 
import com.marathicoder.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
 
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
 
    List<Attendance> findByBatchNameAndDate(String batchName, LocalDate date);
 
    List<Attendance> findByStudentName(String studentName);
    List<Attendance> findByStudentEmail(String email);

    List<Attendance> findByStudentId(String studentId);
 
    List<Attendance> findByTrainerId(String trainerId);
 
    List<Attendance> findByTrainerIdAndBatchNameAndDate(String trainerId, String batchName, LocalDate date);
 
    List<Attendance> findByBatchNameAndDateOrderByStudentNameAsc(String batchName, LocalDate date);
 
    Optional<Attendance> findByStudentIdAndTrainerIdAndBatchNameAndDate(
            String studentId, String trainerId, String batchName, LocalDate date
    );
}
 
 
