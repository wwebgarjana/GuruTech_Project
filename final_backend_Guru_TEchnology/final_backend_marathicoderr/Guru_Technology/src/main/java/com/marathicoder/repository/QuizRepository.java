package com.marathicoder.repository;

import com.marathicoder.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByTrainerId(String trainerId);
    List<Quiz> findByStudentId(String studentId);
    List<Quiz> findByStudentEmail(String studentEmail);
}