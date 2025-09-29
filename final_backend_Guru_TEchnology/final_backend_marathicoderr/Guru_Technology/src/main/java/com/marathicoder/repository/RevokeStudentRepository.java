


package com.marathicoder.repository;

import com.marathicoder.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RevokeStudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentIdAndEmail(String studentId, String email);
}
