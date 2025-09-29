package com.marathicoder.service;

import org.springframework.stereotype.Service;
import com.marathicoder.model.Feedback;
import com.marathicoder.repository.FeedbackRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository repo;

    public FeedbackService(FeedbackRepository repo) {
        this.repo = repo;
    }


    public Feedback save(Feedback feedback) {
        feedback.setCreatedAt(LocalDateTime.now());
        return repo.save(feedback);
    }


    public List<Feedback> findByCourseAndBatch(String course, String batch) {
        if (course != null && !course.isEmpty() && batch != null && !batch.isEmpty()) {
            return repo.findByCourseAndBatch(course, batch);
        } else if (course != null && !course.isEmpty()) {
            return repo.findByCourse(course);
        } else if (batch != null && !batch.isEmpty()) {
            return repo.findByBatch(batch);
        } else {
            return repo.findAll();
        }
    }
}
