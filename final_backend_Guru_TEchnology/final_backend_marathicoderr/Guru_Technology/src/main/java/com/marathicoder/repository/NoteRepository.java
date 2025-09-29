


package com.marathicoder.repository;

import com.marathicoder.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByTrainerId(String trainerId);
    List<Note> findByStudentId(String studentId);
    List<Note> findByStudentEmail(String studentEmail);
}
