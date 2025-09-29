


package com.marathicoder.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentId;     // Unique ID
    private String studentName;
    private String studentEmail;  // ✅ New field added for email-based fetching

    private String trainerId;
    private String batchName;

    private LocalDate date;

    private String status; // Present / Absent

    // Constructors
    public Attendance() {}

    public Attendance(String studentId, String studentName, String trainerId, 
                      String batchName, String studentEmail, 
                      LocalDate date, String status) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.trainerId = trainerId;
        this.batchName = batchName;
        this.studentEmail = studentEmail;
        this.date = date;
        this.status = status;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public String getTrainerId() { return trainerId; }
    public void setTrainerId(String trainerId) { this.trainerId = trainerId; }

    public String getBatchName() { return batchName; }
    public void setBatchName(String batchName) { this.batchName = batchName; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
