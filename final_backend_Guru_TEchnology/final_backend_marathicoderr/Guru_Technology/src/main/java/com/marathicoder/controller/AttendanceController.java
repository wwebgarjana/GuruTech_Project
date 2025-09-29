


package com.marathicoder.controller;

import com.marathicoder.model.Attendance;
import com.marathicoder.model.Student;
import com.marathicoder.model.StudentTrainerAssign;
import com.marathicoder.repository.AttendanceRepository;
import com.marathicoder.repository.StudentRepository;
import com.marathicoder.repository.StudentTrainerAssignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/attendance")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private StudentTrainerAssignRepository assignmentRepo;

    // ✅ Get distinct batches handled by a trainer
    @GetMapping("/batches/{trainerId}")
  public List<String> getBatchesByTrainer(@PathVariable String trainerId) {
      List<StudentTrainerAssign> assignments = assignmentRepo.findByTrainerId(trainerId);

      if (assignments == null || assignments.isEmpty()) {
          return Collections.emptyList();
      }

      return assignments.stream()
              .map(StudentTrainerAssign::getBatches) // 🔥 keep as-is if entity field = batches
              .filter(Objects::nonNull)
              .distinct()
              .collect(Collectors.toList());
  }
    // ✅ Get students assigned to a trainer's batch & check attendance for the date
    @GetMapping("/students/{trainerId}/{batch}/{date}")
    public Map<String, Object> getStudentsByBatchAndDate(
            @PathVariable String trainerId,
            @PathVariable String batch,
            @PathVariable String date
    ) {
        LocalDate parsedDate = LocalDate.parse(date);

     // ✅ All assignments leke filter karna
        List<StudentTrainerAssign> assignedStudents =
                assignmentRepo.findByTrainerId(trainerId).stream()
                    .filter(s -> Arrays.stream(s.getBatches().split(","))
                                       .map(String::trim)
                                       .anyMatch(b -> b.equalsIgnoreCase(batch)))
                    .collect(Collectors.toList());


        Map<String, StudentTrainerAssign> uniqueAssignments = assignedStudents.stream()
                .collect(Collectors.toMap(
                        StudentTrainerAssign::getStudentId,
                        s -> s,
                        (existing, replacement) -> existing
                ));

        List<Attendance> attendanceRecords =
                attendanceRepo.findByTrainerIdAndBatchNameAndDate(trainerId, batch, parsedDate);

        boolean alreadyMarked = !attendanceRecords.isEmpty();

        Map<String, Object> response = new HashMap<>();
        response.put("alreadyMarked", alreadyMarked);

        if (alreadyMarked) {
            response.put("students", attendanceRecords);
        } else {
            // ✅ use setters instead of constructor
            List<Attendance> placeholders = uniqueAssignments.values().stream()
                    .map(s -> {
                        Attendance att = new Attendance();
                        att.setStudentId(s.getStudentId());
                        att.setStudentName(s.getStudentName());
                        att.setStudentEmail(s.getStudentEmail());
                        att.setTrainerId(s.getTrainerId());
                        att.setBatchName(s.getBatches());
                        att.setDate(parsedDate);
                        att.setStatus(null);
                        return att;
                    })
                    .collect(Collectors.toList());
            response.put("students", placeholders);
        }

        return response;
    }

    // ✅ Mark attendance for a batch of students
//    @PostMapping("/mark")
//    public ResponseEntity<Map<String, Object>> markAttendance(@RequestBody List<Attendance> attendanceList) {
//        Map<String, Object> response = new HashMap<>();
//
//        if (attendanceList == null || attendanceList.isEmpty()) {
//            response.put("message", "No attendance data provided");
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        String batchName = attendanceList.get(0).getBatchName();
//        LocalDate date = attendanceList.get(0).getDate();
//        String trainerId = attendanceList.get(0).getTrainerId();
//
//        if (batchName == null || date == null) {
//            response.put("message", "Batch and Date are required");
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        // ✅ If already exists, return existing records
//        List<Attendance> existingRecords =
//                attendanceRepo.findByTrainerIdAndBatchNameAndDate(trainerId, batchName, date);
//
//        if (!existingRecords.isEmpty()) {
//            response.put("message", "Attendance already marked for this batch on " + date);
//            response.put("students", existingRecords);
//            response.put("alreadyMarked", true);
//            return ResponseEntity.ok(response);
//        }
//
//        // ✅ Enrich before save
//        attendanceList.forEach(a -> {
//            if (a.getBatchName() == null) a.setBatchName(batchName);
//            if (a.getDate() == null) a.setDate(date);
//            if (a.getTrainerId() == null) a.setTrainerId(trainerId);
//
//            if (a.getStudentId() != null) {
//                Optional<Student> studentOpt = studentRepo.findByStudentId(a.getStudentId());
//                studentOpt.ifPresent(student -> a.setStudentEmail(student.getEmail()));
//            }
//        });
//
//        attendanceRepo.saveAll(attendanceList);
//
//        response.put("message", "Attendance marked successfully!");
//        response.put("students", attendanceList);
//        response.put("alreadyMarked", false);
//
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/mark")
    public ResponseEntity<Map<String, Object>> markAttendance(@RequestBody List<Attendance> attendanceList) {
        Map<String, Object> response = new HashMap<>();

        if (attendanceList == null || attendanceList.isEmpty()) {
            response.put("message", "No attendance data provided");
            return ResponseEntity.badRequest().body(response);
        }

        String batchName = attendanceList.get(0).getBatchName();
        LocalDate date = attendanceList.get(0).getDate();
        String trainerId = attendanceList.get(0).getTrainerId();

        if (batchName == null || date == null) {
            response.put("message", "Batch and Date are required");
            return ResponseEntity.badRequest().body(response);
        }

        // ✅ Find existing records
        List<Attendance> existingRecords =
                attendanceRepo.findByTrainerIdAndBatchNameAndDate(trainerId, batchName, date);

        // Map for easy lookup by studentId
        Map<String, Attendance> existingMap = new HashMap<>();
        existingRecords.forEach(a -> existingMap.put(a.getStudentId(), a));

        List<Attendance> finalList = new ArrayList<>();

        for (Attendance a : attendanceList) {
            // ✅ Set email properly
            if (a.getStudentId() != null) {
                Optional<Student> studentOpt = studentRepo.findByStudentId(a.getStudentId());
                studentOpt.ifPresent(student -> a.setStudentEmail(student.getEmail()));
            }

            if (existingMap.containsKey(a.getStudentId())) {
                // ✅ Update existing record
                Attendance existing = existingMap.get(a.getStudentId());
                existing.setStatus(a.getStatus());  // update present/absent
                finalList.add(existing);
            } else {
                // ✅ New record
                if (a.getBatchName() == null) a.setBatchName(batchName);
                if (a.getDate() == null) a.setDate(date);
                if (a.getTrainerId() == null) a.setTrainerId(trainerId);

                finalList.add(a);
            }
        }

        attendanceRepo.saveAll(finalList);

        response.put("message", "Attendance saved/updated successfully!");
        response.put("students", finalList);
        response.put("alreadyMarked", true);

        return ResponseEntity.ok(response);
    }

    // ✅ Get all batches (from all trainers)
    @GetMapping("/batches")
    public ResponseEntity<List<String>> getAllBatches() {
        List<StudentTrainerAssign> studentTrainerAssignList = assignmentRepo.findAll();

        List<String> batches = studentTrainerAssignList.stream()
                .map(StudentTrainerAssign::getBatches)
                .filter(Objects::nonNull)
                .flatMap(b -> Stream.of(b.split(",")))
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());

        return ResponseEntity.ok(batches);
    }

    // ✅ Get students by batch + check if attendance already marked for a date
    @GetMapping("/students/{batch}/{date}")
    public ResponseEntity<Map<String, Object>> getStudentsByBatchForDate(
            @PathVariable String batch,
            @PathVariable String date
    ) {
        LocalDate parsedDate = LocalDate.parse(date);

        List<StudentTrainerAssign> studentTrainerAssignList = assignmentRepo.findAll();

        List<StudentTrainerAssign> assignedStudents = studentTrainerAssignList.stream()
                .filter(assign -> assign.getBatches() != null &&
                        Arrays.asList(assign.getBatches().split(",")).contains(batch))
                .collect(Collectors.toList());

        List<Attendance> attendanceRecords =
                attendanceRepo.findByBatchNameAndDate(batch, parsedDate);

        boolean alreadyMarked = !attendanceRecords.isEmpty();

        Map<String, Object> response = new HashMap<>();
        response.put("alreadyMarked", alreadyMarked);

        if (alreadyMarked) {
            response.put("students", attendanceRecords);
        } else {
            // ✅ use setters instead of constructor
            List<Attendance> placeholders = assignedStudents.stream()
                    .map(s -> {
                        Attendance att = new Attendance();
                        att.setStudentId(s.getStudentId());
                        att.setStudentName(s.getStudentName());
                        att.setStudentEmail(s.getStudentEmail());
                        att.setTrainerId(s.getTrainerId());
                        att.setBatchName(s.getBatches());
                        att.setDate(parsedDate);
                        att.setStatus(null);
                        return att;
                    })
                    .collect(Collectors.toList());
            response.put("students", placeholders);
        }

        return ResponseEntity.ok(response);
    }

    // ✅ Get attendance for a particular student using Email
 // ✅ Get attendance for a particular student using Email
    @GetMapping("/student/email/{email}")
    public ResponseEntity<List<Attendance>> getAttendanceByEmail(@PathVariable String email) {
        List<Attendance> attendanceList = attendanceRepo.findByStudentEmail(email);

        if (attendanceList.isEmpty()) {
            return ResponseEntity.notFound().build(); // return 404 if no records
        }

        return ResponseEntity.ok(attendanceList); // return records
    }

}
