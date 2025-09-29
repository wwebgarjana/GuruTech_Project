package com.marathicoder.dto;
 
public class StudentTrainerAssignDto {
    private String studentId;
    private String trainerId;
    private String timeIn;
    private String timeOut;
    private String duration;   // frontend se aayega (optional)
    private String description;
 
    // --- Getters & Setters ---
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
 
    public String getTrainerId() { return trainerId; }
    public void setTrainerId(String trainerId) { this.trainerId = trainerId; }
 
    public String getTimeIn() { return timeIn; }
    public void setTimeIn(String timeIn) { this.timeIn = timeIn; }
 
    public String getTimeOut() { return timeOut; }
    public void setTimeOut(String timeOut) { this.timeOut = timeOut; }
 
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
 
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
 
 