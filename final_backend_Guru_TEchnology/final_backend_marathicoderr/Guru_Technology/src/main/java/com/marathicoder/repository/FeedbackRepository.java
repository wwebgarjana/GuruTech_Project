package com.marathicoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.marathicoder.model.Feedback;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByCourseAndBatch(String course, String batch);
    List<Feedback> findByCourse(String course);
    List<Feedback> findByBatch(String batch);
}
