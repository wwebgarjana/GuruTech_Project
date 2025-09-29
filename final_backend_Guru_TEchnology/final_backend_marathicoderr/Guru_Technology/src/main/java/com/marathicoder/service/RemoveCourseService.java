package com.marathicoder.service;
 
import com.marathicoder.model.Trainer;
import com.marathicoder.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
 
@Service
public class RemoveCourseService {
 
    @Autowired
    private TrainerRepository trainerRepository;
 
    @Transactional
    public String removeCourseFromTrainer(String email, String course) {
        Optional<Trainer> trainers = trainerRepository.findByEmail(email);

        if (trainers.isEmpty()) {
            return "Trainer not found with email: " + email;
        }

        // Unwrap optional before using
        Trainer trainer = trainers.get();

        List<String> courseList = Arrays.stream(trainer.getCourses().split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        if (!courseList.contains(course)) {
            return "Course '" + course + "' not found for trainer with email: " + email;
        }

        courseList.remove(course);
        trainer.setCourses(String.join(", ", courseList));
        trainerRepository.save(trainer);

        return "Course '" + course + "' removed successfully for trainer with email: " + email;
    }}

 
 