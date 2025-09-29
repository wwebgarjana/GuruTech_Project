

package com.marathicoder.controller;
 
import com.marathicoder.model.Student;
import com.marathicoder.model.StudentTrainerAssign;
import com.marathicoder.model.Trainer;
import com.marathicoder.repository.StudentRepository;
import com.marathicoder.repository.StudentTrainerAssignRepository;
import com.marathicoder.repository.TrainerRepository;
import com.marathicoder.service.StudentTrainerAssignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
 
@RestController
@RequestMapping("/assign")
@CrossOrigin(origins = "http://localhost:4200")
public class StudentTrainerAssignController {
 
    @Autowired
    private StudentTrainerAssignService assignService;
 
    @Autowired
    private StudentTrainerAssignRepository assignmentRepo;
 
    @Autowired
    private TrainerRepository trainerRepository;
 
    
    @Autowired
    private StudentRepository studentRepository;
 
    // ✅ FIXED method call: removed trainerEmail, corrected parameter order
    @PostMapping("/trainer")
    public StudentTrainerAssign assignTrainer(@RequestBody StudentTrainerAssign request) {
        return assignService.assignTrainer(
                request.getStudentId(),
                request.getStudentName(),
                request.getTrainerId(),
                request.getCourse(),       // frontend-provided
                request.getBatch(),        // frontend-provided
                request.getTimeIn(),
                request.getTimeOut(),
                request.getDuration(),
                request.getStudentEmail(),
                request.getDescription()
        );
    }
 
    @GetMapping("/all")
    public List<StudentTrainerAssign> getAllAssignments() {
        return assignmentRepo.findAll();
    }
 
    @GetMapping("/student/{studentId}")
    public List<StudentTrainerAssign> getAssignmentsByStudent(@PathVariable String studentId) {
        return assignmentRepo.findByStudentId(studentId);
    }
 
    @GetMapping("/count/{trainerId}")
    public long getStudentCountForTrainer(@PathVariable String trainerId) {
        return assignmentRepo.countUniqueStudentsByTrainerId(trainerId);
    }
 
    // ✅ resolve trainerId from trainerName or trainerId (since trainerEmail is removed)
    @GetMapping("/id-by-email")
    public Map<String, Object> getTrainerByEmail(@RequestParam String email) {
        // search in Trainer table instead of assignment (better practice)
        Optional<com.marathicoder.model.Trainer> trainerOpt = trainerRepository.findByEmail(email);
 
        if (trainerOpt.isEmpty()) {
            return Map.of("trainerId", "", "students", List.of());
        }
 
        String trainerId = trainerOpt.get().getTrainerId();
        List<StudentTrainerAssign> students = assignmentRepo.findByTrainerId(trainerId);
 
        Map<String, Object> response = new HashMap<>();
        response.put("trainerId", trainerId);
        response.put("students", students);
        return response;
    }
 
    // ✅ fallback: get only students by trainerId
    @GetMapping("/trainer/{trainerId}")
    public List<StudentTrainerAssign> getByTrainer(@PathVariable String trainerId) {
        return assignmentRepo.findByTrainerId(trainerId);
    }
    
    
    @GetMapping("/student")
    public List<StudentTrainerAssign> getAssignmentsByEmail(@RequestParam String email) {
        // 1️⃣ Fetch student by email
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found with email: " + email));
 
        String studentId = student.getStudentId(); // get studentId
 
        // 2️⃣ Fetch assignments using studentId
        return assignmentRepo.findByStudentId(studentId);
    }
    @GetMapping("/studentsall")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();   // ✅ अब directly students आएंगे
    }
 
    @GetMapping("/trainersall")
    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }
    
    
    /// trainer cha dashboard la ahe he ///

    @GetMapping("/batchwise-by-email")
    public Map<String, Object> getTrainerInfoByEmail(@RequestParam String email) {
        // find all assignments of this trainer
        List<StudentTrainerAssign> assignments = assignmentRepo.findByTrainerEmail(email);

        if (assignments.isEmpty()) {
            return Map.of("trainerId", "", "batches", List.of(), "batchwiseStudents", Map.of());
        }

        // trainerId is same for all records of that trainer
        String trainerId = assignments.get(0).getTrainerId();

        // collect unique batches
        Set<String> batchSet = new HashSet<>();
        Map<String, List<Map<String, Object>>> batchwiseStudents = new HashMap<>();

        for (StudentTrainerAssign assign : assignments) {
            String batch = assign.getBatch(); // single batch field

            batchSet.add(batch);

            // create student info
            Map<String, Object> studentInfo = new HashMap<>();
            studentInfo.put("studentId", assign.getStudentId());
            studentInfo.put("studentName", assign.getStudentName());
            studentInfo.put("course", assign.getCourse());

            // add student to batch list
            batchwiseStudents.computeIfAbsent(batch, k -> new ArrayList<>()).add(studentInfo);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("trainerId", trainerId);
        response.put("batches", batchSet);
        response.put("batchwiseStudents", batchwiseStudents);

        return response;
    }

}
 
 