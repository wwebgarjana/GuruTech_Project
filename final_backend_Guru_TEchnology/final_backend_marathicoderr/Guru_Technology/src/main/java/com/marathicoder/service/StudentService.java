package com.marathicoder.service;

import com.marathicoder.model.Student;
import com.marathicoder.repository.StudentRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repo;

    // Get last student ID or fallback to ST000
    public String getLastStudentId() {
        String lastId = repo.findLastStudentId();
        return lastId != null ? lastId : "ST000";
    }

    public Optional<Student> findByEmail(String email) {
        return repo.findByEmail(email); // may return null
    }

}
