

package com.marathicoder.model;

import javax.persistence.*;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String batch;
    private String course;
    private String description;
    private String techStack;
    private String assignedDate;
    private String status; // Pending / Locked / Submitted etc.
    private String trainerEmail;
    // Assigned file by trainer
    private String fileName;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileData;

    // Student submission
    private String submissionFileName;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] submissionData;

    // Trainer/Student mapping
    private String trainerId;
    private String studentId;
    private String studentEmail;
    private String studentName;

    public Project() {}

    // Getters & Setters
    
    public Long getId() { return id; }
    public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public void setId(Long id) { this.id = id; }
    public String getBatch() { return batch; }
    public void setBatch(String batch) { this.batch = batch; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTechStack() { return techStack; }
    public void setTechStack(String techStack) { this.techStack = techStack; }
    public String getAssignedDate() { return assignedDate; }
    public void setAssignedDate(String assignedDate) { this.assignedDate = assignedDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public byte[] getFileData() { return fileData; }
    public void setFileData(byte[] fileData) { this.fileData = fileData; }
    public String getSubmissionFileName() { return submissionFileName; }
    public void setSubmissionFileName(String submissionFileName) { this.submissionFileName = submissionFileName; }
    public byte[] getSubmissionData() { return submissionData; }
    public void setSubmissionData(byte[] submissionData) { this.submissionData = submissionData; }
    public String getTrainerId() { return trainerId; }
    public void setTrainerId(String trainerId) { this.trainerId = trainerId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

	public String getTrainerEmail() {
		return trainerEmail;
	}

	public void setTrainerEmail(String trainerEmail) {
		this.trainerEmail = trainerEmail;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}
    
    
}

