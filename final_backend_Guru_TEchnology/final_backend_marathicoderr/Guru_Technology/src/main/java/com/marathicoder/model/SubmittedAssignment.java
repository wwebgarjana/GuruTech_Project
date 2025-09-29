
package com.marathicoder.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submitted_assignment")
public class SubmittedAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long assignmentId;          // Link to Assignment
    private String studentId;           // student identifier
    private String studentEmail;        // student email
    private String studentName;         // student name
    private String fileName;

    @Lob
    @Column(name = "file_data", columnDefinition = "LONGBLOB")
    private byte[] fileData;

    private LocalDateTime submittedDate;

    private String topic; // assignment topic/title

    public SubmittedAssignment() {}

    public SubmittedAssignment(Long assignmentId, String studentId, String studentEmail, String studentName,
                               String fileName, byte[] fileData, LocalDateTime submittedDate, String topic) {
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.studentEmail = studentEmail;
        this.studentName = studentName;
        this.fileName = fileName;
        this.fileData = fileData;
        this.submittedDate = submittedDate;
        this.topic = topic;
    }

    // --------------- Getters & Setters ---------------
    public Long getId() { return id; }

    public Long getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Long assignmentId) { this.assignmentId = assignmentId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public byte[] getFileData() { return fileData; }
    public void setFileData(byte[] fileData) { this.fileData = fileData; }

    public LocalDateTime getSubmittedDate() { return submittedDate; }
    public void setSubmittedDate(LocalDateTime submittedDate) { this.submittedDate = submittedDate; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
}
