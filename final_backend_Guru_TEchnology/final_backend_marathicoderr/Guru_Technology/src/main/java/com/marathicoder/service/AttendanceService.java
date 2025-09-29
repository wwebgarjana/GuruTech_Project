

package com.marathicoder.service;
 
import com.marathicoder.model.Attendance;
import com.marathicoder.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
 
@Service
public class AttendanceService {
 
    @Autowired
    private AttendanceRepository attendanceRepository;
 
    public Attendance markAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }
 
    public void markAttendanceBulk(List<Attendance> attendanceList) {
        attendanceRepository.saveAll(attendanceList);
    }
 
    public List<Attendance> getAttendanceByBatchAndDate(String batchName, LocalDate date) {
        return attendanceRepository.findByBatchNameAndDate(batchName, date);
    }
 
    public List<Attendance> getAttendanceByStudent(String studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
 
    public List<Attendance> getAttendanceByTrainer(String trainerId) {
        return attendanceRepository.findByTrainerId(trainerId);
    }
 
    public Optional<Attendance> getAttendanceRecord(String studentId, String trainerId, String batchName, LocalDate date) {
        return attendanceRepository.findByStudentIdAndTrainerIdAndBatchNameAndDate(studentId, trainerId, batchName, date);
    }
 
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }
}
 
 
