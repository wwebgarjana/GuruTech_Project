


package com.marathicoder.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marathicoder.model.StudentTrainerAssign;
import com.marathicoder.model.Trainer;
import com.marathicoder.repository.StudentTrainerAssignRepository;
import com.marathicoder.repository.TrainerRepository;

import java.util.Optional;

@Service
public class StudentTrainerAssignService {

    @Autowired
    private TrainerRepository trainerRepo;

    @Autowired
    private StudentTrainerAssignRepository assignmentRepo;

    public StudentTrainerAssign assignTrainer(
            String studentId,
            String studentName, // frontend-provided student name
            String trainerId,
            String course,
            String batch,
            String timeIn,
            String timeOut,
            String duration,
            String studentEmail,
            String description) {

        Optional<Trainer> trainerOpt = trainerRepo.findByTrainerId(trainerId);

        if (trainerOpt.isPresent()) {
            Trainer trainer = trainerOpt.get();

            // 🔍 Duplication check
            Optional<StudentTrainerAssign> existing = assignmentRepo
                    .findByStudentIdAndTrainerIdAndTrainerCourseAndBatches(
                            studentId, trainerId, trainer.getCourses(), trainer.getBatches());
            if (existing.isPresent()) {
                throw new RuntimeException("Student already assigned to this trainer for same course and batch!");
            }

            StudentTrainerAssign assignment = new StudentTrainerAssign();
            assignment.setStudentId(studentId);
            assignment.setStudentName(studentName); // <-- use frontend name
            assignment.setTrainerId(trainerId);
            assignment.setCourse(course);  // now explicitly set
            assignment.setBatch(batch); 
            assignment.setTrainerName(trainer.getName());
            assignment.setStudentEmail(studentEmail); 
            assignment.setTrainerEmail(trainer.getEmail());
            assignment.setTrainerCourse(trainer.getCourses());
            assignment.setBatches(batch);
            assignment.setTimeIn(timeIn);
            assignment.setTimeOut(timeOut);
            assignment.setDescription(description);

            if (duration != null && !duration.isEmpty()) {
                assignment.setDuration(duration);
            } else {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    LocalTime in = LocalTime.parse(timeIn, formatter);
                    LocalTime out = LocalTime.parse(timeOut, formatter);
                    Duration dur = Duration.between(in, out);
                    long hours = dur.toHours();
                    long minutes = dur.toMinutes() % 60;
                    assignment.setDuration(hours + "h " + minutes + "m");
                } catch (Exception e) {
                    assignment.setDuration("Invalid time");
                }
            }

            assignment.setAssignedDate(LocalDate.now().toString());
            return assignmentRepo.save(assignment);
        }

        throw new RuntimeException("Trainer not found!");
    }

	public StudentTrainerAssign assignTrainer(String studentId, String studentName, String trainerId,
			String trainerName, String course, String batch, String timeIn, String timeOut, String duration) {
		// TODO Auto-generated method stub
		return null;
	}

	public Optional<StudentTrainerAssign> findByStudentIdAndBatch(String studentId, String batch) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//Optional<StudentTrainerAssign> findByStudentIdAndBatch(String studentId, String batch);

}
