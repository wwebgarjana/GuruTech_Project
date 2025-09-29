

package com.marathicoder.service;

import com.marathicoder.model.Project;
import com.marathicoder.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    // Assign project to a student
    public Project assignProject(String batch, String course, String description, String techStack, String assignedDate,
                                 MultipartFile file, String trainerId, String studentId, String studentEmail, String trainerEmail) throws IOException {
        Project p = new Project();
        p.setBatch(batch);
        p.setCourse(course);
        p.setDescription(description);
        p.setTechStack(techStack);
        p.setAssignedDate(assignedDate);
        p.setStatus("Pending");
        p.setTrainerId(trainerId);
        p.setStudentId(studentId);
        p.setStudentEmail(studentEmail);
        p.setTrainerEmail(trainerEmail);

        if (file != null && !file.isEmpty()) {
            p.setFileName(file.getOriginalFilename());
            p.setFileData(file.getBytes());
        }
        return projectRepository.save(p);
    }

    // Submit student file
    public Project submitProject(Long projectId, MultipartFile submissionFile) throws IOException {
        Optional<Project> optional = projectRepository.findById(projectId);
        if (optional.isPresent()) {
            Project p = optional.get();
            p.setSubmissionFileName(submissionFile.getOriginalFilename());
            p.setSubmissionData(submissionFile.getBytes());
            p.setStatus("Submitted");
            return projectRepository.save(p);
        }
        return null;
    }

    public List<Project> getAllProjects() { return projectRepository.findAll(); }
    public Optional<Project> getProjectById(Long id) { return projectRepository.findById(id); }
    public void deleteProject(Long id) { projectRepository.deleteById(id); }
    public List<Project> getAllSubmittedProjects() { return projectRepository.findBySubmissionDataIsNotNull(); }
    public List<Project> getProjectsByTrainer(String trainerId) { return projectRepository.findByTrainerId(trainerId); }
    public List<Project> getProjectsByStudent(String studentId) { return projectRepository.findByStudentId(studentId); }
    public List<Project> getProjectsByStudentEmail(String studentEmail) { return projectRepository.findByStudentEmail(studentEmail); }
}
