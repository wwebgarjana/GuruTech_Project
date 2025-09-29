

package com.marathicoder.controller;

import com.marathicoder.dto.EducationUpdateRequest;
import com.marathicoder.model.CourseBatch;
import com.marathicoder.model.Trainer;
import com.marathicoder.model.UserEntity;
import com.marathicoder.repository.CourseBatchRepository;
import com.marathicoder.repository.TrainerRepository;
import com.marathicoder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/trainers")
@CrossOrigin(origins = "http://localhost:4200")
public class TrainerController {

    @Autowired
    private TrainerRepository trainerRepo;

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CourseBatchRepository courseBatchRepo;
    // ================= Register Trainer =================
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerTrainer(@RequestBody Trainer trainer) {
        Map<String, String> response = new HashMap<>();

        if (trainerRepo.existsByMobile(trainer.getMobile())) {
            response.put("error", "Mobile number already registered");
            return ResponseEntity.status(409).body(response);
        }

        if (trainerRepo.existsByAadhaar(trainer.getAadhaar())) {
            response.put("error", "Aadhaar number already registered");
            return ResponseEntity.status(409).body(response);
        }

        // ✅ Normalize email
        trainer.setEmail(trainer.getEmail().trim().toLowerCase());

        trainerRepo.save(trainer);

        // Save login credentials
        UserEntity login = new UserEntity();
        login.setEmail(trainer.getEmail());
        login.setPassword(trainer.getPassword());
        login.setRole("trainer");
        userRepo.save(login);

        response.put("message", "Trainer registered and login access given");
        return ResponseEntity.ok(response);
    }

   
 // ================= Trainer Summaries =================
    @GetMapping("/summary")
    public ResponseEntity<List<Map<String, Object>>> getTrainerSummaries() {
        List<Trainer> trainers = trainerRepo.findAll();
        List<Map<String, Object>> summaries = new ArrayList<>();

        for (Trainer t : trainers) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("trainerId", t.getTrainerId());
            data.put("name", t.getName());
            data.put("email", t.getEmail());
            data.put("status", t.getStatus());

            // ✅ Fetch from CourseBatch table instead of static t.getCourses()
            List<CourseBatch> courseBatches = courseBatchRepo.findByTrainer(t);

            // Unique courses
            Set<String> courses = new HashSet<>();
            // Unique batches
            Set<String> batches = new HashSet<>();

            for (CourseBatch cb : courseBatches) {
                courses.add(cb.getCourseName());
                batches.add(cb.getBatchName());
            }

            data.put("courses", courses);
            data.put("batches", batches);

            // ✅ Extra stats
            data.put("totalCourses", courses.size());
            data.put("totalAssignments", courseBatches.size()); // course+batch pairs count

            summaries.add(data);
        }

        return ResponseEntity.ok(summaries);
    }

    // ================= Profile =================
    @GetMapping("/profile")
    public ResponseEntity<?> getTrainerProfile(@RequestParam String email) {
        String normalizedEmail = email.trim().toLowerCase();
        Optional<Trainer> trainerOpt = trainerRepo.findByEmail(normalizedEmail);

        if (trainerOpt.isPresent()) {
            Trainer t = trainerOpt.get();
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("trainerId", t.getTrainerId());
            data.put("collegeId", t.getCollegeId());
            data.put("name", t.getName());
            data.put("aadhaar", t.getAadhaar());
            data.put("mobile", t.getMobile());
            data.put("birthdate", t.getBirthdate());
            data.put("address", t.getAddress());
            data.put("courses", t.getCourses());
            data.put("batches", t.getBatches());
            data.put("email", t.getEmail());
            data.put("education", t.getEducation());
            data.put("experience", t.getExperience());
            data.put("startDate", t.getStartDate());
            data.put("endDate", t.getEndDate());
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.status(404).body("Trainer not found");
        }
    }

    // ================= Check Email Exists =================
    @GetMapping("/exists-by-email")
    public boolean checkEmailExists(@RequestParam String email) {
        return trainerRepo.existsByEmail(email.trim().toLowerCase());
    }

    // ================= Update Education =================
    @PostMapping("/update-education")
    public ResponseEntity<?> updateEducation(@RequestBody EducationUpdateRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        Optional<Trainer> trainerOpt = trainerRepo.findByEmail(normalizedEmail);

        if (trainerOpt.isPresent()) {
            Trainer trainer = trainerOpt.get();
            trainer.setEducation(request.getEducation());
            trainerRepo.save(trainer);
            return ResponseEntity.ok("Education updated successfully!");
        } else {
            return ResponseEntity.status(404).body("Trainer not found");
        }
    }

    // ================= Update Experience =================
    @PostMapping("/update-experience")
    public ResponseEntity<?> updateExperience(@RequestBody Map<String, Object> body) {
        String email = body.get("email").toString().trim().toLowerCase();
        List<String> experience = (List<String>) body.get("experience");

        Optional<Trainer> trainerOpt = trainerRepo.findByEmail(email);
        if (trainerOpt.isPresent()) {
            Trainer trainer = trainerOpt.get();
            trainer.setExperience(experience);
            trainerRepo.save(trainer);
            return ResponseEntity.ok("Experience updated successfully!");
        } else {
            return ResponseEntity.status(404).body("Trainer not found");
        }
    }
    
    @GetMapping("/last-id")
    public String getLastTrainerId() {
        Trainer lastTrainer = trainerRepo.findTopByOrderByTrainerIdDesc();
        return lastTrainer != null ? lastTrainer.getTrainerId() : "TG000";
    }
    
    
 // ================= Extra: Get Email by TrainerId =================
    @GetMapping("/email-by-id")
    public ResponseEntity<?> getEmailByTrainerId(@RequestParam String trainerId) {
        Optional<Trainer> trainerOpt = trainerRepo.findByTrainerId(trainerId);
        if (trainerOpt.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("trainerId", trainerOpt.get().getTrainerId());
            response.put("email", trainerOpt.get().getEmail());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body("Trainer not found for ID: " + trainerId);
        }
    }

    // ================= Extra: Get TrainerId by Email =================
    @GetMapping("/id-by-email")
    public ResponseEntity<?> getTrainerIdByEmail(@RequestParam String email) {
        Optional<Trainer> trainerOpt = trainerRepo.findByEmail(email.trim().toLowerCase());
        if (trainerOpt.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("trainerId", trainerOpt.get().getTrainerId());
            response.put("email", trainerOpt.get().getEmail());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body("Trainer not found for email: " + email);
        }
    }


//add-course code


 
    
//    
//    @PostMapping("/assignCourseBatch")
//    public ResponseEntity<?> assignCourseBatch(@RequestParam String email,
//                                               @RequestParam String course,
//                                               @RequestParam String batch) {
//        // normalize email
//        String normalizedEmail = email.trim().toLowerCase();
//        Optional<Trainer> trainerOpt = trainerRepo.findByEmail(normalizedEmail);
//
//        if (trainerOpt.isPresent()) {
//            Trainer trainer = trainerOpt.get();
//
//            // 🔹 Check if this batch is already assigned to any trainer
//            Optional<CourseBatch> existingBatch = courseBatchRepo.findByCourseNameAndBatchName(course, batch);
//            if (existingBatch.isPresent()) {
//                return ResponseEntity.status(409).body("Batch '" + batch + "' for course '" + course + "' is already assigned to another trainer!");
//            }
//
//            // New course+batch save
//            CourseBatch cb = new CourseBatch();
//            cb.setCourseName(course);
//            cb.setBatchName(batch);
//            cb.setTrainer(trainer);
//
//            courseBatchRepo.save(cb);
//
//            // Assigned list
//            List<CourseBatch> assignedList = courseBatchRepo.findByTrainer(trainer);
//
//            Map<String, Object> response = new LinkedHashMap<>();
//            response.put("message", "New course & batch assigned to trainer successfully!");
//            response.put("trainerName", trainer.getName());
//            response.put("email", trainer.getEmail());
//            response.put("totalAssigned", assignedList.size());
//
//            // course+batch details
//            List<Map<String, String>> details = new ArrayList<>();
//            for (CourseBatch c : assignedList) {
//                Map<String, String> d = new HashMap<>();
//                d.put("course", c.getCourseName());
//                d.put("batch", c.getBatchName());
//                details.add(d);
//            }
//            response.put("assignedCourses", details);
//
//            return ResponseEntity.ok(response);
//
//        } else {
//            return ResponseEntity.status(404).body("Trainer not found with email: " + email);
//        }
//    }
 // ================= Assign Unique Batch (One Batch -> One Trainer) =================
    @PostMapping("/assignUniqueBatch")
    public ResponseEntity<?> assignUniqueBatch(@RequestParam String email,
                                               @RequestParam String course,
                                               @RequestParam String batch) {
        String normalizedEmail = email.trim().toLowerCase();
        Optional<Trainer> trainerOpt = trainerRepo.findByEmail(normalizedEmail);

        if (trainerOpt.isPresent()) {
            Trainer trainer = trainerOpt.get();

            // 🔹 Check if this batch is already assigned to ANY trainer
            Optional<CourseBatch> existingBatch = courseBatchRepo.findByBatchName(batch);
            if (existingBatch.isPresent()) {
                return ResponseEntity.status(409).body(
                        "Batch '" + batch + "' is already assigned to trainer: "
                                + existingBatch.get().getTrainer().getName());
            }

            // 🔹 Save new course+batch mapping in CourseBatch table
            CourseBatch cb = new CourseBatch();
            cb.setCourseName(course);
            cb.setBatchName(batch);
            cb.setTrainer(trainer);
            courseBatchRepo.save(cb);

            // 🔹 Fetch all assignments of this trainer
            List<CourseBatch> assignedList = courseBatchRepo.findByTrainer(trainer);

            // 🔹 Collect unique courses & batches
            Set<String> updatedCourses = new HashSet<>();
            Set<String> updatedBatches = new HashSet<>();
            for (CourseBatch c : assignedList) {
                updatedCourses.add(c.getCourseName());
                updatedBatches.add(c.getBatchName());
            }

            // 🔹 Update Trainer table fields also
            trainer.setCourses(String.join(",", updatedCourses));
            trainer.setBatches(String.join(",", updatedBatches));
            trainerRepo.save(trainer);

            // 🔹 Prepare response
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "Batch assigned successfully to trainer!");
            response.put("trainerName", trainer.getName());
            response.put("email", trainer.getEmail());
            response.put("totalAssigned", assignedList.size());

            List<Map<String, String>> details = new ArrayList<>();
            for (CourseBatch c : assignedList) {
                Map<String, String> d = new HashMap<>();
                d.put("course", c.getCourseName());
                d.put("batch", c.getBatchName());
                details.add(d);
            }
            response.put("assignedCourses", details);

            return ResponseEntity.ok(response);

        } else {
            return ResponseEntity.status(404).body("Trainer not found with email: " + email);
        }
    }

    @GetMapping("/email/{email}/assignments")
    public ResponseEntity<?> getTrainerAssignments(@PathVariable String email) {
        Trainer trainer = trainerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));

        List<Map<String, String>> assignments = new ArrayList<>();

        if (trainer.getCourseBatches() != null) {
            for (CourseBatch cb : trainer.getCourseBatches()) {
                Map<String, String> data = new HashMap<>();
                data.put("course", cb.getCourseName());
                data.put("batch", cb.getBatchName());
                assignments.add(data);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("trainerName", trainer.getName());
        response.put("totalAssignments", assignments.size());
        response.put("assignments", assignments);

        return ResponseEntity.ok(response);
    }
//    @DeleteMapping("/removeCourseBatch")
//    public ResponseEntity<?> removeCourseBatch(@RequestParam String email,
//                                               @RequestParam String course,
//                                               @RequestParam String batch) {
//        String normalizedEmail = email.trim().toLowerCase();
//        Optional<Trainer> trainerOpt = trainerRepo.findByEmail(normalizedEmail);
//
//        if (trainerOpt.isPresent()) {
//            Trainer trainer = trainerOpt.get();
//
//            // Find course+batch for this trainer
//            Optional<CourseBatch> cbOpt = courseBatchRepo.findByTrainerAndCourseNameAndBatchName(
//                    trainer, course, batch);
//
//            if (cbOpt.isPresent()) {
//                courseBatchRepo.delete(cbOpt.get());
//
//                // ✅ Remaining list निकाल
//                List<CourseBatch> assignedList = courseBatchRepo.findByTrainer(trainer);
//
//                Map<String, Object> response = new LinkedHashMap<>();
//                response.put("message", "Course & Batch removed successfully!");
//                response.put("trainerName", trainer.getName());
//                response.put("email", trainer.getEmail());
//                response.put("totalAssigned", assignedList.size());
//
//                List<Map<String, String>> details = new ArrayList<>();
//                for (CourseBatch c : assignedList) {
//                    Map<String, String> d = new HashMap<>();
//                    d.put("course", c.getCourseName());
//                    d.put("batch", c.getBatchName());
//                    details.add(d);
//                }
//                response.put("assignedCourses", details);
//
//                return ResponseEntity.ok(response);
//
//            } else {
//                return ResponseEntity.status(404).body("No such assignment found!");
//            }
//        } else {
//            return ResponseEntity.status(404).body("Trainer not found with email: " + email);
//        }
//    }
//    
    @DeleteMapping("/removeCourseBatch")
    public ResponseEntity<?> removeCourseBatch(@RequestParam String email,
                                               @RequestParam String course,
                                               @RequestParam String batch) {
        String normalizedEmail = email.trim().toLowerCase();
        Optional<Trainer> trainerOpt = trainerRepo.findByEmail(normalizedEmail);

        if (trainerOpt.isPresent()) {
            Trainer trainer = trainerOpt.get();

            // Find course+batch for this trainer
            Optional<CourseBatch> cbOpt = courseBatchRepo.findByTrainerAndCourseNameAndBatchName(
                    trainer, course, batch);

            if (cbOpt.isPresent()) {
                courseBatchRepo.delete(cbOpt.get());

                // ✅ Remaining list काढा
                List<CourseBatch> assignedList = courseBatchRepo.findByTrainer(trainer);

                // ✅ Trainer टेबल मधले courses & batches अपडेट करा
                Set<String> updatedCourses = new HashSet<>();
                Set<String> updatedBatches = new HashSet<>();
                for (CourseBatch c : assignedList) {
                    updatedCourses.add(c.getCourseName());
                    updatedBatches.add(c.getBatchName());
                }

                trainer.setCourses(String.join(",", updatedCourses));
                trainer.setBatches(String.join(",", updatedBatches));
                trainerRepo.save(trainer);

                // ✅ Response
                Map<String, Object> response = new LinkedHashMap<>();
                response.put("message", "Course & Batch removed successfully!");
                response.put("trainerName", trainer.getName());
                response.put("email", trainer.getEmail());
                response.put("totalAssigned", assignedList.size());

                List<Map<String, String>> details = new ArrayList<>();
                for (CourseBatch c : assignedList) {
                    Map<String, String> d = new HashMap<>();
                    d.put("course", c.getCourseName());
                    d.put("batch", c.getBatchName());
                    details.add(d);
                }
                response.put("assignedCourses", details);

                return ResponseEntity.ok(response);

            } else {
                return ResponseEntity.status(404).body("No such assignment found!");
            }
        } else {
            return ResponseEntity.status(404).body("Trainer not found with email: " + email);
        }
    }

    //manage course assign course
    
    
    
    @GetMapping("/id/{trainerId}/assignments")
    public ResponseEntity<?> getTrainerAssignmentsById(@PathVariable String trainerId) {
        Trainer trainer = trainerRepo.findByTrainerId(trainerId)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));

        List<Map<String, String>> assignments = new ArrayList<>();
        if (trainer.getCourseBatches() != null) {
            for (CourseBatch cb : trainer.getCourseBatches()) {
                Map<String, String> data = new HashMap<>();
                data.put("course", cb.getCourseName());
                data.put("batch", cb.getBatchName());
                assignments.add(data);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("assignments", assignments);

        return ResponseEntity.ok(response);
    }

}