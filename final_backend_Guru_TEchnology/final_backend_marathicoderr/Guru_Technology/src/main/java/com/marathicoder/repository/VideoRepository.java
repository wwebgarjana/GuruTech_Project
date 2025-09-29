


package com.marathicoder.repository;

import com.marathicoder.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByTrainerId(String trainerId);
    List<Video> findByStudentId(String studentId);
    List<Video> findByStudentEmail(String studentEmail);
}
