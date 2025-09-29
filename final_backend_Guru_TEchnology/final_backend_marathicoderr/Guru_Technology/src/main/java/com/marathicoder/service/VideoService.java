

package com.marathicoder.service;

import com.marathicoder.model.Video;
import com.marathicoder.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    public Video uploadVideo(String title, String description, MultipartFile file,
                             String trainerId, String studentId, String studentEmail) throws IOException {
        Video video = new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setFileName(file.getOriginalFilename());
        video.setData(file.getBytes());

        // ✅ Assign metadata
        video.setTrainerId(trainerId);
        video.setStudentId(studentId);
        video.setStudentEmail(studentEmail);

        return videoRepository.save(video);
    }

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    public List<Video> getVideosByTrainer(String trainerId) {
        return videoRepository.findByTrainerId(trainerId);
    }

    public List<Video> getVideosByStudent(String studentId) {
        return videoRepository.findByStudentId(studentId);
    }

    public List<Video> getVideosByStudentEmail(String studentEmail) {
        return videoRepository.findByStudentEmail(studentEmail);
    }

    public Optional<Video> getVideoById(Long id) {
        return videoRepository.findById(id);
    }
}

