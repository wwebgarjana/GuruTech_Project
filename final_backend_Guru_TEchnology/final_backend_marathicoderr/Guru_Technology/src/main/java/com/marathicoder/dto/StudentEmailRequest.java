package com.marathicoder.dto;

public class StudentEmailRequest {
    private String email;
    private String password;
    private String name;       // Full Name
    private String studentId;  // Student ID

    // Constructors
    public StudentEmailRequest() {}

    public StudentEmailRequest(String email, String password, String name, String studentId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.studentId = studentId;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}