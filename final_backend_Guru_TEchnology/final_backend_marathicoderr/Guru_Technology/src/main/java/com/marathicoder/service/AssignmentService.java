

package com.marathicoder.service;

import com.marathicoder.model.Assignment;
import com.marathicoder.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;



 // AssignmentService.java
    public Assignment uploadAssignment(String topic, String description, LocalDate dueDate, MultipartFile file,
                                       String trainerId, String studentId, String studentEmail, String trainerEmail) throws IOException {
        Assignment assignment = new Assignment();
        assignment.setTopic(topic);
        assignment.setDescription(description);
        assignment.setDueDate(dueDate);
        assignment.setFileName(file.getOriginalFilename());
        assignment.setFileData(file.getBytes());

        assignment.setTrainerId(trainerId);
        assignment.setStudentId(studentId);
        assignment.setStudentEmail(studentEmail);
        assignment.setTrainerEmail(trainerEmail); // ✅ add trainer email

        return assignmentRepository.save(assignment);
    }


    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public List<Assignment> getAssignmentsByTrainer(String trainerId) {
        return assignmentRepository.findByTrainerId(trainerId);
    }

    public List<Assignment> getAssignmentsByStudent(String studentId) {
        return assignmentRepository.findByStudentId(studentId);
    }

    public List<Assignment> getAssignmentsByStudentEmail(String studentEmail) {
        return assignmentRepository.findByStudentEmail(studentEmail);
    }

    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }
}
