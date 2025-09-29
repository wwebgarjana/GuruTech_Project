package com.marathicoder.service;

import com.marathicoder.model.SubmittedAssignment;
import com.marathicoder.repository.SubmittedAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubmittedAssignmentService {

    @Autowired
    private SubmittedAssignmentRepository repository;

    public SubmittedAssignment save(SubmittedAssignment submission) {
        if (submission.getSubmittedDate() == null) {
            submission.setSubmittedDate(java.time.LocalDateTime.now());
        }
        return repository.save(submission);
    }

    public List<SubmittedAssignment> getAll() {
        return repository.findAll();
    }

    public Optional<SubmittedAssignment> getById(Long id) {
        return repository.findById(id);
    }

    public List<SubmittedAssignment> getByAssignmentId(Long assignmentId) {
        return repository.findByAssignmentId(assignmentId);
    }
}
